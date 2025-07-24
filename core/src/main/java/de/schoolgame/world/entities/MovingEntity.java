package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import de.schoolgame.primitives.*;
import de.schoolgame.render.Sound;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;
import de.schoolgame.world.WorldObject;

import java.util.ArrayList;
import java.util.List;

public abstract class MovingEntity extends Entity {
    public static final float DEFAULT_GRAVITY = -25.0f;
    public static final float GROUND_FRICTION = 2f;
    public static final float AIR_FRICTION = 2f;

    public ArrayList<Rectf> list = new ArrayList<>();

    public static float GRAVITY = DEFAULT_GRAVITY;

    public static final Vec2f MAX_GROUND_VELOCITY = new Vec2f(6f, 20f);
    public static final Vec2f MAX_AIR_VELOCITY = new Vec2f(10f, 20f);

    public static final Vec2f MAX_GROUND_VELOCITY_BOSS = new Vec2f(3f, 20f);
    public static final Vec2f MAX_AIR_VELOCITY_BOSS = new Vec2f(4, 20f);


    protected Vec2f velocity;
    protected Vec2f acceleration;

    public MovingEntity(Vec2f position, Vec2f size) {
        super(position, size);
        this.velocity = new Vec2f(0.0f, 0.0f);
        this.acceleration = new Vec2f(0.0f, GRAVITY);
    }

    public Rectf getHitbox(){
        return new Rectf(position.cpy(),size.cpy());
        //return new Rectf(position.add(size.mul(0.05f,0.0f)),size.mul(0.9f,0.9f));
    }

    @Override
    public void update() {
        var state = GameState.INSTANCE;
        var worldSize = state.world.getSize();

        velocity = velocity.add(acceleration.scl(Gdx.graphics.getDeltaTime()));
        Vec2f targetPosition = position.add(velocity.scl(Gdx.graphics.getDeltaTime()));

        if ((this instanceof PlayerEntity pe && pe.getDead()) ||
            (this instanceof FriedrichEntity fe && fe.getDead()) ||
            (this instanceof KoenigEntity ke && ke.getDead()) ||
            (this instanceof EichelsbacherEntity ee && ee.getDead()))
        {
            this.position = targetPosition;
            return;
        }

        if ((position.y < 0.0f && GRAVITY < 0.0f) || (position.y > worldSize.y - size.y && GRAVITY > 0.0f)) {
            switch (this) {
                case PlayerEntity pe -> {
                    position.y = (GRAVITY < 0.0f ? 0.0f : worldSize.y - size.y);
                    pe.kill();
                }
                case FriedrichEntity pe -> {
                    position.y = (GRAVITY < 0.0f ? 0.0f : worldSize.y - size.y);
                    pe.kill();
                }
                case KoenigEntity pe -> {
                    position.y = (GRAVITY < 0.0f ? 0.0f : worldSize.y - size.y);
                    pe.kill();
                }
                case EichelsbacherEntity pe -> {
                    position.y = (GRAVITY < 0.0f ? 0.0f : worldSize.y - size.y);
                    pe.kill();
                }
                default -> state.world.getEntities().remove(this);
            }
            return;
        }

        if(!(this instanceof PlayerEntity pe && pe.getGodmode())){
            targetPosition = rayCollisionFast(targetPosition,worldSize.toVec2f());
        }
        if(this instanceof PlayerEntity pe && !pe.getGodmode()){
            rayCollisionPlayerEntity(pe,targetPosition);
        }
        this.position = targetPosition;

        worldCollision(worldSize);
    }

    private void worldCollision(Vec2i worldSize) {
        if (position.x <= 0.0f) {
            position.x = 0.0f;
            onCollision(Direction.RIGHT,new Vec2i(0,0),WorldObject.WORLD_BORDER);
        }
        if (position.x >= worldSize.x - size.x) {
            position.x = worldSize.x - size.x;
            onCollision(Direction.LEFT,new Vec2i((int)(worldSize.x - size.x),0),WorldObject.WORLD_BORDER);

            if(this instanceof PlayerEntity && GameState.INSTANCE.getState() == GameState.GameStateType.GAME){
                Sound sound = GameState.INSTANCE.assetManager.get("audio/complete/complete", Sound.class);
                sound.play();

                GameState.INSTANCE.setState(GameState.GameStateType.WORLD_SELECT);
                return;
            }
        }
        if (position.y <= 0.0f && GRAVITY > 0.0f) {
            position.y = 0.0f;
            velocity.y = 0.0f;
        }
        if (position.y >= worldSize.y - size.y && GRAVITY < 0.0f) {
            position.y = worldSize.y - size.y;
            onCollision(Direction.DOWN,new Vec2i(0,(int)(worldSize.y - size.y)),WorldObject.WORLD_BORDER);
        }
    }

    private void rayCollisionPlayerEntity(PlayerEntity player,Vec2f target) {
        List<Entity> entities = GameState.INSTANCE.world.getEntities();

        for(int i = 0;i<entities.size();i++){
            Entity entity = entities.get(i);
            Rectf playerRectf = player.getRect();

            Vec2f dir = target.sub(player.position);
            if(dir.len()==0.0f){
                if(playerRectf.overlap(entity.getRect())){
                    Direction collisionDirection = playerRectf.getDirection(entity.getRect());

                    if(player.onEntityCollision(entity,collisionDirection)){
                        entities.remove(i);
                        i--;
                    }
                }
            }else{
                ContactWrapper cw = playerRectf.RI_Solver(target,entity.getRect());
                if (cw != null && cw.d!=Direction.NONE) {
                    if(player.onEntityCollision(entity,cw.d)){
                        entities.remove(i);
                        i--;
                    }
                }else if(playerRectf.overlap(entity.getRect())){
                    if(player.onEntityCollision(entity,playerRectf.getDirection(entity.getRect()))){
                        entities.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    private Vec2f rayCollisionFast(Vec2f pos, Vec2f worldSize) {
        list.clear();

        var myRect = getHitbox();

        ArrayList<CollisionObject> potentialCollisions = findTileCollisions(
            myRect.pos,
            pos,
            myRect.size,
            worldSize
        );
	    sortCollisionsByDistance(potentialCollisions,myRect);

        boolean doesntStamp = true;
        if(this instanceof PlayerEntity pe) doesntStamp = !pe.getStamp();

        Vec2f cp = pos.cpy();
        for (CollisionObject co : potentialCollisions) {
            ContactWrapper cw = myRect.RI_Solver(cp, co.rectf);

            assert co.type.getTile() != null;
            if (cw != null && cw.d != Direction.NONE &&
                (co.type.getTile().collisiontype != Direction.UP || cw.d == Direction.UP && doesntStamp) &&
                (co.type.getTile().collisiontype == Direction.ALL || co.type.getTile().collisiontype == cw.d)){
                onCollision(cw.d,co.rectf.pos.toVec2i(),co.type);
                cp = cw.cp.cpy();
            }
        }

        potentialCollisions = findTileCollisions(
            myRect.pos,
            pos,
            myRect.size,
            worldSize
        );
	    sortCollisionsByDistance(potentialCollisions,myRect);

        for (CollisionObject co : potentialCollisions) {
            if(myRect.overlap(co.rectf)){
                Rectf r = new Rectf(cp.cpy(),myRect.size.cpy());
                Direction d = r.staticCollisionSolver(co.rectf);

                assert co.type.getTile() != null;
                if(d != Direction.NONE &&
                  (co.type.getTile().collisiontype != Direction.UP || d == Direction.UP && doesntStamp && velocity.y<0.0f) &&
                  (co.type.getTile().collisiontype == Direction.ALL || co.type.getTile().collisiontype == d)){
                    onCollision(d,co.rectf.pos.toVec2i(),co.type);
                    cp = r.pos.cpy();
                }
            }
        }

	    return cp;
    }

    private ArrayList<CollisionObject> findTileCollisions(Vec2f from,Vec2f to,Vec2f size,Vec2f worldSize) {
        ArrayList<CollisionObject> collisions = new ArrayList<>();

        final Vec2f direction = to.sub(from);
        final Vec2f searchArea = size.add(direction.abs()).max(new Vec2f(1.0f,1.0f));
        final Vec2f start = from.min(to).sub(searchArea).clamp(new Vec2f(0.0f,0.0f),worldSize);
        final Vec2f end = from.max(to).add(searchArea).clamp(new Vec2f(0.0f,0.0f),worldSize);

        final Vec2i n_start = start.toVec2iF();
        final Vec2i n_end = end.toVec2iC();

        for (int x = n_start.x; x < n_end.x; x++) {
            for (int y = n_start.y; y < n_end.y; y++) {
                Rectf tileRectf = new Rectf(new Vec2f(x,y),new Vec2f(1.0f,1.0f));

                WorldObject worldObject = GameState.INSTANCE.world.at(new Vec2i(x,y));
                if (worldObject != WorldObject.NONE && worldObject.isTile()) {
                    //if (sweptRect.overlap(tileRect))
                    collisions.add(new CollisionObject(tileRectf, Direction.NONE, worldObject));
                }
            }
        }
        return collisions;
    }

    private void sortCollisionsByDistance(ArrayList<CollisionObject> collisions, Rectf entityRectf) {
        collisions.sort((r1,r2) -> {
            Vec2f me = entityRectf.mid();
            float d1 = r1.rectf.mid().sub(me).len();
            float d2 = r2.rectf.mid().sub(me).len();
            return Float.compare(d1,d2);
        });
    }


    abstract void onCollision(Direction direction,Vec2i pos,WorldObject object);

    private static class CollisionObject {
        public final Rectf rectf;
        public Direction direction;
        public final WorldObject type;

        public CollisionObject(Rectf rectf, Direction direction, WorldObject type) {
            this.rectf = rectf;
            this.direction = direction;
            this.type = type;
        }
    }
}
