package de.schoolgame.world.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.TileSet;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Tile;

public class Concrete extends Tile {
    @Override
    public void render(Batch batch, Vec2f drawPosition, Vec2i worldPosition) {
        var state = GameState.INSTANCE;

        int tileSize = state.world.getTileSize();
        byte connections = state.world.connectionsAt(worldPosition);
        TileSet tileSet = state.assetManager.get("tiles/concrete/concrete", TileSet.class);
        TextureRegion textureRegion = tileSet.getTextureRegion(connections);

        batch.draw(textureRegion, drawPosition.x, drawPosition.y, tileSize, tileSize);
    }
}
