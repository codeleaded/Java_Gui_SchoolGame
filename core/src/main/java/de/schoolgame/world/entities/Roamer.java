package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.primitives.Direction;
import de.schoolgame.utils.primitives.Vec2f;

import static de.schoolgame.utils.primitives.Direction.*;

public class Roamer extends MovingEntity {
    private static final Texture roamerTexture = new Texture(Gdx.files.internal("entities/roamer/roamer.png"));

    public Roamer(Vec2f position) {
        super(position, new Vec2f(0.95f, 0.95f));
        velocity.x = 3f;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    void onCollision(Direction type) {
        if (type == LEFT || type == RIGHT) velocity.x = -velocity.x;
        if (type == UP && velocity.y < 0.0f) velocity.y = 0.0f;
    }

    @Override
    public void render(Batch batch) {
        var tileSize = GameState.INSTANCE.world.getTileSize();
        batch.draw(roamerTexture, position.x * tileSize, position.y * tileSize,
            size.x * tileSize, size.y * tileSize,
            0, 0, tileSize, tileSize, velocity.x < 0.0f, false);
    }

    @Override
    public void dispose() {
        roamerTexture.dispose();
    }
}
