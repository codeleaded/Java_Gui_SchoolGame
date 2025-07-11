package de.schoolgame.world.entities;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.Gdx;

import de.schoolgame.primitives.ContactWrapper;
import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;
import de.schoolgame.world.WorldObject;

public abstract class MovingEntity extends Entity {
    public static final float DEFAULT_GRAVITY = -25.0f;
    public static final float GROUND_FRICTION = 2f;
    public static final float AIR_FRICTION = 2f;
    public static final float RC_STEP_CHANGE = 0.001f;

    public ArrayList<Rect> list = new ArrayList<>();

    public static float GRAVITY = DEFAULT_GRAVITY;

    public static final Vec2f MAX_GROUND_VELOCITY = new Vec2f(6f, 20f);
    public static final Vec2f MAX_AIR_VELOCITY = new Vec2f(10f, 20f);

    protected Vec2f velocity;
    protected Vec2f acceleration;

    public MovingEntity(Vec2f position, Vec2f size) {
        super(position, size);
        this.velocity = new Vec2f(0.0f, 0.0f);
        this.acceleration = new Vec2f(0.0f, GRAVITY);
    }

    @Override
    public void update() {
        var state = GameState.INSTANCE;
        var worldSize = state.world.getSize();

        velocity = velocity.add(acceleration.scl(Gdx.graphics.getDeltaTime()));
        Vec2f targetposition = position.add(velocity.scl(Gdx.graphics.getDeltaTime()));

        if (this instanceof PlayerEntity pe && pe.getDead()) {
            this.position = targetposition;
            return;
        }

        if ((position.y < 0.0f && GRAVITY < 0.0f) || (position.y > worldSize.y - size.y && GRAVITY > 0.0f)) {
            if (this instanceof PlayerEntity pe) {
                position.y = (GRAVITY < 0.0f ? 0.0f : worldSize.y - size.y);
                pe.kill();
                return;
            }

            state.world.getEntities().remove(this);
            return;
        }

        rayCollision(targetposition,worldSize);
        //rayCollisionFast(targetposition,worldSize.toVec2f()); //TODO: Funktioniert in C, hier zum laufen kriegen...
        worldCollision(worldSize);
    }

    private void worldCollision(Vec2i worldSize) {
        if (position.x <= 0.0f) {
            position.x = 0.0f;
            onCollision(Direction.RIGHT, WorldObject.WORLD_BORDER);
        }
        if (position.x >= worldSize.x - size.x) {
            position.x = worldSize.x - size.x;
            onCollision(Direction.LEFT, WorldObject.WORLD_BORDER);
        }
        if (position.y <= 0.0f && GRAVITY > 0.0f) {
            position.y = 0.0f;
            velocity.y = 0.0f;
        }
        if (position.y >= worldSize.y - size.y && GRAVITY < 0.0f) {
            position.y = worldSize.y - size.y;
            onCollision(Direction.DOWN, WorldObject.WORLD_BORDER);
        }
    }

    private void rayCollisionFast(Vec2f pos, Vec2f worldSize) {
        list.clear();
        final Vec2f direction = pos.sub(position);
        var searchArea = size.add(direction.abs()).toVec2i().max(Vec2i.ONE).mul(3).toVec2f();

        var searchStart = position.min(pos).sub(searchArea).clamp(Vec2f.ZERO, worldSize);
        var searchEnd = position.max(pos).add(searchArea).clamp(Vec2f.ZERO, worldSize);


        System.out.println(position+" "+pos);
        System.out.println(searchStart+" "+searchEnd);

        var myRect = getRect();

        ArrayList<CollisionObject> potentialCollisions = findTileCollisions(
            searchStart.toVec2i(),
            searchEnd.toVec2i(),
            position,
            pos
        );
	    sortCollisionsByDistance(potentialCollisions,myRect);

        System.out.println(potentialCollisions.size() + " pot collisions found");

        Vec2f cp = pos.cpy();
        for (CollisionObject co : potentialCollisions) {
            list.add(co.rect);

            ContactWrapper cw = myRect.RI_Solver(cp, co.rect);
            if (cw != null) {
                onCollision(cw.d, co.type);
                cp = cw.cp;
            }
        }

	    position = cp;
    }

    private ArrayList<CollisionObject> findTileCollisions(Vec2i start, Vec2i end, Vec2f from, Vec2f to) {
        ArrayList<CollisionObject> collisions = new ArrayList<>();

        // Swept AABB: Covers both start and end rects
        Rect sweptRect = new Rect(
            from.min(to),
            size.add(from.sub(to).abs())
        );

        for (int x = start.x; x < end.x; x++) {
            for (int y = start.y; y < end.y; y++) {
                Rect tileRect = new Rect(new Vec2f(x, y), new Vec2f(1.0f, 1.0f));

                WorldObject worldObject = GameState.INSTANCE.world.at(new Vec2i(x, y));
                if (worldObject != WorldObject.NONE && worldObject.isTile()) {

                    if (sweptRect.overlap(tileRect)) {
                        collisions.add(new CollisionObject(tileRect, Direction.NONE, worldObject));
                    }
                }
            }
        }
        return collisions;
    }

    private void rayCollision(Vec2f pos, Vec2i worldSize) {
        final Vec2f direction = pos.sub(position);
        final float pathLength = direction.len();

        ArrayList<CollisionObject> collisionObjects = new ArrayList<>();

        if (pathLength == 0.0f) {
            detectCollisions(collisionObjects, worldSize);
        } else {
            performRaycast(direction, pathLength, collisionObjects, worldSize);
        }

        int size = 0;
        CollisionObject[] dirs = new CollisionObject[Direction.values().length - 1];
        for (CollisionObject collision : collisionObjects) {
            int index = collision.direction.ordinal() - 1;
            if (index >= 0 && dirs[index] == null) {
                dirs[index] = collision;
                if (size == 4) break;
                size++;
            }
        }

        for (CollisionObject collision : dirs) {
            if (collision == null) continue;
            onCollision(collision.direction, collision.type);
        }
    }

    private void performRaycast(Vec2f direction, float pathLength, ArrayList<CollisionObject> collisionObjects, Vec2i worldSize) {
        final Vec2f rdir = direction.norm().mul(RC_STEP_CHANGE);

        for (float step = 0.0f; step <= pathLength; step += RC_STEP_CHANGE) {
            position = position.add(rdir);
            detectCollisions(collisionObjects, worldSize);
        }
    }

    private void detectCollisions(ArrayList<CollisionObject> collisionObjects, Vec2i worldSize) {
        var entityPosition = position.toVec2i();
        var searchArea = size.toVec2i().max(new Vec2i(1, 1)).mul(3);
        var searchStart = entityPosition.sub(searchArea).clamp(Vec2i.ZERO, worldSize);
        var searchEnd = entityPosition.add(searchArea).clamp(Vec2i.ZERO, worldSize);
        var entityRect = getRect();

        ArrayList<CollisionObject> potentialCollisions = findTileCollisions(searchStart, searchEnd);

        if (this instanceof PlayerEntity player) {
            handlePlayerEntityCollisions(player);
        }

        sortCollisionsByDistance(potentialCollisions, getRect());

        potentialCollisions.forEach(collision -> {
            if (!entityRect.overlap(collision.rect)) {
                return;
            }
            Direction collisionDirection = entityRect.staticCollisionSolver(collision.rect);
            if (collisionDirection == Direction.NONE) {
                return;
            }
            collision.direction = collisionDirection;

            collisionObjects.add(collision);
        });
    }

    private ArrayList<CollisionObject> findTileCollisions(Vec2i start, Vec2i end) {
        ArrayList<CollisionObject> collisions = new ArrayList<>();
        var entityRect = getRect();

        for (int x = start.x; x < end.x; x++) {
            for (int y = start.y; y < end.y; y++) {
                Rect tileRect = new Rect(new Vec2f(x, y), new Vec2f(1.0f, 1.0f));

                WorldObject worldObject = GameState.INSTANCE.world.at(new Vec2i(x, y));
                if (worldObject != WorldObject.NONE && worldObject.isTile()) {

                    if (entityRect.overlap(tileRect)) {
                        collisions.add(new CollisionObject(tileRect, Direction.NONE, worldObject));
                    }
                }
            }
        }
        return collisions;
    }

    private void handlePlayerEntityCollisions(PlayerEntity player) {
        Collection<Entity> entities = GameState.INSTANCE.world.getEntities();
        entities.removeIf(entity -> {
            Rect playerRect = player.getRect();
            if (playerRect.overlap(entity.getRect())) {
                Direction collisionDirection = playerRect.getDirection(entity.getRect());
                return player.onEntityCollision(entity, collisionDirection);
            }
            return false;
        });
    }

    private void sortCollisionsByDistance(ArrayList<CollisionObject> collisions, Rect entityRect) {
        collisions.sort((r1, r2) -> {
            float distance1 = r1.rect.pos.sub(entityRect.pos).len();
            float distance2 = r2.rect.pos.sub(entityRect.pos).len();
            return Float.compare(distance1, distance2);
        });
    }


    abstract void onCollision(Direction direction, WorldObject object);

    private static class CollisionObject {
        public final Rect rect;
        public Direction direction;
        public final WorldObject type;

        public CollisionObject(Rect rect, Direction direction, WorldObject type) {
            this.rect = rect;
            this.direction = direction;
            this.type = type;
        }
    }
}
