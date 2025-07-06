package de.schoolgame.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.state.GameState;

public abstract class Entity {
    protected Vec2f position;
    protected Vec2f size;

    public abstract void update();
    public abstract void render(Batch batch);
    public void dispose() {}

    public Entity(Vec2f position,Vec2f size) {
        this.position = position;
        this.size = size;
    }

    public Vec2f getPosition() {
        return position;
    }

    public Vec2f getPixelPosition() {
        return position.mul(GameState.INSTANCE.world.getTileSize());
    }

    public Rect getRect(){
        return new Rect(position, size);
    }
}
