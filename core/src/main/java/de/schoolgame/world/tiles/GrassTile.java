package de.schoolgame.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.TileSet;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Tile;

public class GrassTile extends Tile {
    private final TileSet tileSet;

    public GrassTile() {
        super();
        var assetManager = GameState.INSTANCE.assetManager;
        Texture texture = assetManager.get("tiles/patrikarts/tileset-grass.png", Texture.class);
        tileSet = new TileSet(texture, new Vec2i(32, 32));
    }

    @Override
    public void render(Batch batch, Vec2f drawPosition, Vec2i worldPosition) {
        var tileSize = GameState.INSTANCE.world.getTileSize();
        var connections = GameState.INSTANCE.world.connectionsAt(worldPosition);
        var textureRegion = tileSet.getTextureRegion(connections);
        batch.draw(textureRegion, drawPosition.x, drawPosition.y, tileSize, tileSize);
    }
}
