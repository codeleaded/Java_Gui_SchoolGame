package de.schoolgame.world.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.Animation;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Tile;

public class QuestMark extends Tile {
    private long startTime;

    public QuestMark() {
        startTime = System.nanoTime();
    }

    @Override
    public void render(Batch batch, Vec2f drawPosition, Vec2i worldPosition) {
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();

        double elapsedTime = (double)(System.nanoTime() - startTime) / 1000_000_000.0;
        
        Animation animation = state.assetManager.get("tiles/questmark/questmark", Animation.class);

        batch.draw(animation.currentFrame((float)elapsedTime), drawPosition.x, drawPosition.y, tileSize, tileSize);
    }
}
