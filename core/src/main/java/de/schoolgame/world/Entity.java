package de.schoolgame.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    protected Vector2 position;

    public abstract void update();
    public abstract void render(Batch batch);
    public abstract void dispose();

    public Entity(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }
}
