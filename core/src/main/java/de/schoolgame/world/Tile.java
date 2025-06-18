package de.schoolgame.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;

public abstract class Tile {
    public abstract void render(Batch batch, Vec2f drawPosition, Vec2i worldPosition);
    public void dispose() {}
}
