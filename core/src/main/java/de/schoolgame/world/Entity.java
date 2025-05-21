package de.schoolgame.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.primitives.Vec2f;

public abstract class Entity {
    protected Vec2f position;

    public abstract void update();
    public abstract void render(Batch batch);
    public abstract void dispose();

    public Entity(Vec2f position) {
        this.position = position;
    }

    public Vec2f getPosition() {
        return position;
    }

    public Vec2f getPixelPosition() {
        return position.mul(GameState.INSTANCE.world.getTileSize());
    }
}
