package de.schoolgame.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Tile;

public class Laboratorytable extends Tile {
    @Override
    public void render(Batch batch, Vec2f drawPosition, Vec2i worldPosition) {
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();
        Texture texture = state.assetManager.get("tiles/laboratorytable/laboratorytable", Texture.class);
        batch.draw(texture, drawPosition.x, drawPosition.y, tileSize, tileSize);
    }
}
