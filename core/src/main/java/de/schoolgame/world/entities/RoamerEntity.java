package de.schoolgame.world.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.state.GameState;
import de.schoolgame.world.WorldObject;

import static de.schoolgame.primitives.Direction.*;

public class RoamerEntity extends MovingEntity {
    public RoamerEntity(Vec2f position) {
        super(position, new Vec2f(0.95f, 0.95f));
        velocity.x = 3f;
    }

    @Override
    public void update() {
        this.acceleration = new Vec2f(0.0f, GRAVITY);
        super.update();
    }

    @Override
    void onCollision(Direction type,  WorldObject object) {
        if (type == LEFT || type == RIGHT) velocity.x = -velocity.x;
        if (type == UP && velocity.y < 0.0f) velocity.y = 0.0f;
    }

    @Override
    public void render(Batch batch) {
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();
        Texture texture = state.assetManager.get("entities/roamer/roamer", Texture.class);

        batch.draw(texture, position.x * tileSize, position.y * tileSize,
            size.x * tileSize, size.y * tileSize,
            0, 0, tileSize, tileSize, velocity.x < 0.0f, MovingEntity.GRAVITY > 0.0f);
    }
}
