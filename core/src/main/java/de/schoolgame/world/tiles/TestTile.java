package de.schoolgame.world.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Tile;

public class TestTile extends Tile {
    private final Texture texture = new Texture("tiles/test.png");

    @Override
    public void render(Batch batch, Vec2f drawPosition, Vec2i worldPosition) {
        var tileSize = GameState.INSTANCE.world.getTileSize();
        batch.draw(texture, drawPosition.x, drawPosition.y, tileSize, tileSize);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
