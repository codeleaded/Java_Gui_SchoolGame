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
import java.util.Collection;

public abstract class MovingEntity extends Entity {
    public static final float DEFAULT_GRAVITY = -25.0f;
    public static final float GROUND_FRICTION = 2f;
    public static final float AIR_FRICTION = 2f;
    public static final float RC_STEP_CHANGE = 0.001f;

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

        if (position.y <= 0.0f) {
            if (this instanceof PlayerEntity pe) {
                pe.kill();
                return;
            } else {
                Collection<Entity> entities = state.world.getEntities();
                entities.remove(this);
                return;
            }
        }

        velocity = velocity.add(acceleration.scl(Gdx.graphics.getDeltaTime()));
        Vec2f targetposition = position.add(velocity.scl(Gdx.graphics.getDeltaTime()));
        rayCollision(targetposition, worldSize);
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

        removeOverlappingCollisions(collisionObjects);

        collisionObjects.forEach((collisionObject) -> onCollision(collisionObject.direction, collisionObject.type));
    }

    private void removeOverlappingCollisions(ArrayList<CollisionObject> collisionObjects) {
        for (int i = 0; i < collisionObjects.size(); i++) {
            Rect r1 = collisionObjects.get(i).rect;
            for (int j = i + 1; j < collisionObjects.size(); j++) {
                Rect r2 = collisionObjects.get(j).rect;

                if (r1.compareInt(r2)) {
                    collisionObjects.remove(j);
                    j--;
                }
            }
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
        var searchStart = entityPosition.sub(searchArea);
        var searchEnd = entityPosition.add(searchArea);

        ArrayList<CollisionObject> potentialCollisions = findTileCollisions(searchStart, searchEnd, worldSize);

        if (this instanceof PlayerEntity player) {
            handlePlayerEntityCollisions(player);
        }

        sortCollisionsByDistance(potentialCollisions, getRect());

        var entityRect = getRect();
        potentialCollisions.removeIf(collision -> {
            if (!entityRect.overlap(collision.rect)) {
                return true;
            }
            Direction collisionDirection = entityRect.staticCollisionSolver(collision.rect);
            if (collisionDirection == Direction.NONE) {
                return true;
            }
            collision.direction = collisionDirection;

            collisionObjects.add(collision);
            return false;
        });
    }

    private ArrayList<CollisionObject> findTileCollisions(Vec2i start, Vec2i end, Vec2i worldSize) {
        ArrayList<CollisionObject> collisions = new ArrayList<>();
        var entityRect = getRect();

        for (int x = start.x; x < end.x; x++) {
            for (int y = start.y; y < end.y; y++) {
                if (!isValidWorldPosition(x, y, worldSize)) {
                    continue;
                }

                WorldObject worldObject = GameState.INSTANCE.world.at(new Vec2i(x, y));
                if (worldObject != WorldObject.NONE && worldObject.isTile()) {
                    Rect tileRect = new Rect(new Vec2f(x, y), new Vec2f(1.0f, 1.0f));
                    if (entityRect.overlap(tileRect)) {
                        collisions.add(new CollisionObject(tileRect, Direction.NONE, worldObject));
                    }
                }
            }
        }
        return collisions;
    }

    private boolean isValidWorldPosition(int x, int y, Vec2i worldSize) {
        return x >= 0 && y >= 0 && x <= worldSize.x && y <= worldSize.y;
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
