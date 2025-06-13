package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.state.GameState;

import static de.schoolgame.primitives.Direction.*;

public class Player extends MovingEntity {
    private static final Texture playerTexture = new Texture(Gdx.files.internal("entities/player/player.png"));
    private boolean onGround;
    private boolean onJump;

    public Player(Vec2f pos) {
        super(pos, new Vec2f(0.95f, 0.95f));
        this.onGround = false;
        this.onJump = false;
    }

    public void setJump(boolean jump) {
        this.onJump = jump;
    }

    public boolean move(Direction direction) {
        return switch (direction) {
            case UP:
                if (onGround) {
                    velocity.y = 8;
                    yield true;
                }
            case LEFT:
                acceleration.x = -5;
                yield true;
            case RIGHT:
                acceleration.x = 5;
                yield true;
            case NONE:
                acceleration.x = 0;
            default: yield false;
        };
    }

    @Override
    public void update() {
        float friction = AIR_FRICTION;
        if (onGround) friction += GROUND_FRICTION;

        float sign = velocity.x > 0 ? 1 : -1;
        friction *= -sign * Gdx.graphics.getDeltaTime();
        velocity.x += friction;

        // Zero crossing point
        if (sign != (velocity.x > 0 ? 1 : -1)) velocity.x = 0;

        if(onJump) this.acceleration.y = GRAVITY * 0.33f;
        else       this.acceleration.y = GRAVITY;

        super.update();

        if (onGround) {
            velocity = velocity.clamp(MAX_GROUND_VELOCITY.neg(), MAX_GROUND_VELOCITY);
        } else {
            velocity = velocity.clamp(MAX_AIR_VELOCITY.neg(), MAX_AIR_VELOCITY);
        }

        var state = GameState.INSTANCE;
        // Delete Coin
        state.world.getEntities().removeIf(e -> e instanceof Coin && getRect().overlap(new Rect(e.getPosition(), new Vec2f(0.9f, 0.9f))));
    }

    @Override
    void onCollision(Direction type) {
        onGround = false;
        if (type == UP && velocity.y < 0.0f) velocity.y = 0.0f;
        if (type == DOWN && velocity.y > 0.0f) velocity.y = 0.0f;
        if (type == LEFT || type == RIGHT) velocity.x = 0.0f;
        if (type == UP) onGround = true;
    }

    @Override
    public void render(Batch batch) {
        var tileSize = GameState.INSTANCE.world.getTileSize();
        batch.draw(playerTexture, position.x * tileSize, position.y * tileSize, size.x * tileSize, size.y * tileSize);
    }

    @Override
    public void dispose() {
        playerTexture.dispose();
    }
}
