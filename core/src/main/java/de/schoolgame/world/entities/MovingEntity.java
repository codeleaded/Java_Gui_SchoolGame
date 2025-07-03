package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;

import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;
import de.schoolgame.world.WorldObject;

import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class MovingEntity extends Entity {
    public static final float GRAVITY = -25.0f;
    public static final float GROUND_FRICTION = 2f;
    public static final float AIR_FRICTION = 2f;

    public static final Vec2f MAX_GROUND_VELOCITY = new Vec2f(6f, 20f);
    public static final Vec2f MAX_AIR_VELOCITY = new Vec2f(10f, 20f);

    protected Vec2f size;
    protected Vec2f velocity;
    protected Vec2f acceleration;

    public MovingEntity(Vec2f position, Vec2f size) {
        super(position);
        this.size = size;
        this.velocity = new Vec2f(0.0f, 0.0f);
        this.acceleration = new Vec2f(0.0f, GRAVITY);
    }

    @Override
    public void update() {
        var state = GameState.INSTANCE;
        var worldSize = state.world.getSize();

        if (position.y <= 0.0f) {
            Vec2i sp = state.world.getSpawn();
            position.x = (float)sp.x;
            position.y = (float)sp.y;
        }

        velocity = velocity.add(acceleration.scl(Gdx.graphics.getDeltaTime()));
        Vec2f targetposition = position.add(velocity.scl(Gdx.graphics.getDeltaTime()));
        RayCollision(targetposition,worldSize);

        /*

        Sortierung fällt bei Ray Collisions raus!
        Durch Raycasting erhält man die Rects eh in der richtigen Reihenfolge

        ArrayList<Rect> rects = new ArrayList<>();

        var rect = getRect();

        rects.stream()
            .sorted((r1, r2) -> {
                float d1 = r1.pos.sub(rect.pos).len();
                float d2 = r2.pos.sub(rect.pos).len();
                return Float.compare(d1, d2);
            })
            .map(rect::staticCollisionSolver)
            .distinct()
            .filter(type -> type != NONE)
            .forEach(this::onCollision);

        this.position = rect.pos;
        */
    }

    public void RayCollision(Vec2f pos,Vec2i worldSize){
        final float stepchange = 0.001f;
        final Vec2f dir = pos.sub(position);
        final float length = dir.len();
        
        ArrayList<Rect> retrects = new ArrayList<>();
        ArrayList<Direction> retdirs = new ArrayList<>();

        if(length==0.0f){
            Search(retrects,retdirs,worldSize);
        }else{
            final Vec2f rdir = dir.norm().mul(stepchange);

            for(float step = 0.0f;step<=length;step+=stepchange){
                position = position.add(rdir);
                Search(retrects,retdirs,worldSize);
            }
        }

        for(int i = 0;i<retrects.size();i++){
            Rect r1 = retrects.get(i);
            for(int j = i+1;j<retrects.size();j++){
                Rect r2 = retrects.get(j);

                if(r1.compareInt(r2)){
                    retrects.remove(j);
                    retdirs.remove(j);
                    j--;
                }
            }
        }

        retdirs.forEach(this::onCollision);
    }
    public void Search(ArrayList<Rect> retrects,ArrayList<Direction> retdirs,Vec2i worldSize){
        var posi = position.toVec2i();
        
        var state = GameState.INSTANCE;
        var search = size.toVec2i()
            .max(new Vec2i(1, 1))
            .mul(3);
        var start = posi.sub(search);
        var end = posi.add(search);

        ArrayList<Rect> rects = new ArrayList<>();

        var rect = getRect();

        for (int x = start.x; x < end.x; x += 1) {
            for (int y = start.y; y < end.y; y += 1) {
                if (y < 0 || x < 0) continue;
                if (y > worldSize.y || x > worldSize.x) continue;
                
                WorldObject o = state.world.at(new Vec2i(x,y));
                if (o != WorldObject.NONE && o.isTile()) {
                    Rect r = new Rect(new Vec2f(x,y),new Vec2f(1.0f,1.0f));
                    if(rect.overlap(r)) rects.add(r);
                }
            }
        }

        rects.sort((r1, r2) -> {
            float d1 = r1.pos.sub(rect.pos).len();
            float d2 = r2.pos.sub(rect.pos).len();
            return Float.compare(d1, d2);
        });
        rects.removeIf(r -> {
            if(!rect.overlap(r)) return true;

            Direction d = rect.staticCollisionSolver(r);
            if(d == Direction.NONE) return true;

            retrects.add(r);
            retdirs.add(d);

            return false;
        });
    }

    abstract void onCollision(Direction type);

    public Rect getRect(){
        return new Rect(position, size);
    }
}
