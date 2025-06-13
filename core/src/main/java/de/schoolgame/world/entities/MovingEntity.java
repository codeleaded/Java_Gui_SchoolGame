package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;
import de.schoolgame.world.Tile;

import java.util.ArrayList;

import static de.schoolgame.primitives.Direction.NONE;

public abstract class MovingEntity extends Entity {
    public static final float GRAVITY = -25.0f;
    public static final float GROUND_FRICTION = 20f;
    public static final float AIR_FRICTION = 2f;

    public static final Vec2f MAX_GROUND_VELOCITY = new Vec2f(6f, 20f);
    public static final Vec2f MAX_AIR_VELOCITY = new Vec2f(10f, 20f);

    protected Vec2f size;
    protected Vec2f velocity;
    protected Vec2f acceleration;

    public MovingEntity(Vec2f position) {
        this(position, new Vec2f(1f, 1f));
    }

    public MovingEntity(Vec2f position, Vec2f size) {
        super(position);
        this.size = size;
        this.velocity = new Vec2f(0.0f, 0.0f);
        this.acceleration = new Vec2f(0.0f, GRAVITY);
    }

    @Override
    public void update() {
        velocity = velocity.add(acceleration.scl(Gdx.graphics.getDeltaTime()));
        position = position.add(velocity.scl(Gdx.graphics.getDeltaTime()));

        if (position.y <= 0.0f) {
            velocity.y = 0.0f;
            position.y = 1.0f;
        }

        var state = GameState.INSTANCE;
        var worldSize = state.world.getSize();
        var pos = position.toVec2i();

        var search = size.toVec2i()
            .max(new Vec2i(1, 1))
            .mul(2);
        var start = pos.sub(search);
        var end = pos.add(search);

        ArrayList<Rect> rects = new ArrayList<>();
        for (int x = start.x; x < end.x; x += 1) {
            for (int y = start.y; y < end.y; y += 1) {

                if (y < 0 || x < 0) continue;
                if (y > worldSize.y || x > worldSize.x) continue;

                Tile b = state.world.at(new Vec2i(x, y));
                if (b != Tile.NONE && b.isDrawable()) {
                    rects.add(new Rect(new Vec2f(x, y), new Vec2f(1.0f, 1.0f)));
                }
            }
        }

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
    }

    abstract void onCollision(Direction type);

    protected Rect getRect(){
        return new Rect(position, size);
    }
}
