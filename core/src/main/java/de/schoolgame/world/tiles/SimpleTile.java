package de.schoolgame.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Tile;

public class SimpleTile extends Tile {
    private final String asset;

    public SimpleTile(String asset) {
        super();

        this.asset = asset;
    }
    public SimpleTile(Direction collisiontype,String asset) {
        super(collisiontype);
        this.asset = asset;
    }

    @Override
    public void render(Batch batch, Vec2f drawPosition, Vec2i worldPosition) {
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();
        Texture texture = state.assetManager.get(asset, Texture.class);
        batch.draw(texture, drawPosition.x, drawPosition.y, tileSize, tileSize);
    }
}
