package de.schoolgame.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import de.schoolgame.state.GameState;
import de.schoolgame.world.World;

public class WorldRenderer implements IRenderer {
    private final Batch batch;
    private final Rectangle bounds;

    public WorldRenderer() {
        this.batch = new SpriteBatch();
        this.bounds = new Rectangle();
    }

    public void setView(Camera camera) {
        float width = camera.viewWidth * camera.zoom;
        float height = camera.viewHeight * camera.zoom;

        this.bounds.set(camera.position.x - (width / 2),
            camera.position.y - (height / 2),
            camera.viewWidth, camera.viewHeight);

        batch.setProjectionMatrix(camera.viewProjectionMatrix);
    }

    @Override
    public void render() {
        final World world = GameState.INSTANCE.world;
        final int width = world.getWidth();
        final int height = world.getHeight();
        final int tileSize = world.getTileSize();

        final int start_col = Math.max(0, (int) bounds.x / tileSize);
        final int end_col = Math.min(width, (int) ((bounds.x + bounds.width) / tileSize) + 1);

        final int start_row =  Math.max(0, (int) bounds.y / tileSize);
        final int end_row = Math.min(height, (int) ((bounds.y + bounds.height) / tileSize) + 1);

        batch.begin();
        float y = start_row * tileSize;
        for (int row = start_row; row < end_row; row++) {
            float x = start_col * tileSize;
            for (int col = start_col; col < end_col; col++) {
                var tile = world.at(col, row);
                var texture = tile.getTexture();

                if (texture == null) {
                    x += tileSize;
                    continue;
                }

                batch.draw(texture, x, y);

                x += tileSize;
            }
            y += tileSize;
        }

        world.getEntities()
            .stream()
            .filter(e ->
                e.getPosition().x >= bounds.x - tileSize && e.getPosition().x <= bounds.x + bounds.width &&
                e.getPosition().y >= bounds.y - tileSize && e.getPosition().y <= bounds.y + bounds.height
            )
            .forEach(e -> e.render(batch));

        GameState.INSTANCE.player.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
