package de.schoolgame.world.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.TileSet;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Tile;

public class TileSetTile extends Tile {
    private final String asset;

    public TileSetTile(String asset) {
        super();
        
        this.asset = asset;
    }
    public TileSetTile(Direction collisiontype,String asset) {
        super(collisiontype);
        this.asset = asset;
    }


    @Override
    public void render(Batch batch, Vec2f drawPosition, Vec2i worldPosition) {
        var state = GameState.INSTANCE;

        int tileSize = state.world.getTileSize();
        byte connections = state.world.connectionsAt(worldPosition);
        TileSet tileSet = state.assetManager.get(asset, TileSet.class);
        TextureRegion textureRegion = tileSet.getTextureRegion(connections);

        batch.draw(textureRegion, drawPosition.x, drawPosition.y, tileSize, tileSize);
    }
}
