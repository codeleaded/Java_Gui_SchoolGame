package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.schoolgame.primitives.Direction;
import static de.schoolgame.primitives.Direction.DOWN;
import static de.schoolgame.primitives.Direction.UP;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.state.GameState;

public class Player extends MovingEntity {
    private static final Texture playerTexture = new Texture(Gdx.files.internal("entities/player/player.png"));
    private boolean onGround;
    private boolean onJump;

    public Player(Vec2f pos) {
        super(pos, new Vec2f(0.95f, 0.95f));
        this.onGround = false;
        this.onJump = false;
    }

    public boolean GetJump() {
        return this.onJump;
    }
    public void SetJump(boolean jump) {
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
                velocity.x = -5;
                yield true;
            case RIGHT:
                velocity.x = 5;
                yield true;
            case NONE:
                velocity.x = 0;
            default: yield false;
        };
    }

    @Override
    public void update() {
        onGround = false;

        if(onJump) this.acceleration.y = GRAVITY * 0.33f;
        else       this.acceleration.y = GRAVITY;

        super.update();

        var state = GameState.INSTANCE;
        // Delete Coin
        state.world.getEntities().removeIf(e -> e instanceof Coin && getRect().overlap(new Rect(e.getPosition(), new Vec2f(0.9f, 0.9f))));
    }

    @Override
    void onCollision(Direction type) {
        if (type == UP && velocity.y < 0.0f) velocity.y = 0.0f;
        if (type == DOWN && velocity.y > 0.0f) velocity.y = 0.0f;
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
