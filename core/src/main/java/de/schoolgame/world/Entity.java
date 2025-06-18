package de.schoolgame.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.state.GameState;

public abstract class Entity {
    protected Vec2f position;

    public abstract void update();
    public abstract void render(Batch batch);
    public void dispose() {}

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
