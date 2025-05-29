package de.schoolgame.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.world.World;

public class WorldRenderer implements IRenderer {
    private final Batch batch;
    private Rect bounds;

    public WorldRenderer() {
        this.batch = new SpriteBatch();
        this.bounds = new Rect();
    }

    public void setView(Camera camera) {
        var pos = camera.position.toVec2f().sub(camera.viewSize.toVec2f().mul(camera.zoom).div(2));

        this.bounds = new Rect(pos, camera.viewSize.toVec2f());

        batch.setProjectionMatrix(camera.viewProjectionMatrix);
    }

    @Override
    public void render() {
        final World world = GameState.INSTANCE.world;
        final int tileSize = world.getTileSize();

        Vec2i start = bounds.pos.toVec2i()
            .div(tileSize)
            .max(Vec2i.ZERO);
        Vec2i end = bounds.end().toVec2i()
            .div(tileSize).add(Vec2i.ONE)
            .min(world.getSize());

        batch.begin();
        float y = start.y * tileSize;
        for (int row = start.y; row < end.y; row++) {
            float x = start.x * tileSize;
            for (int col = start.x; col < end.x; col++) {
                var tile = world.at(new Vec2i(col, row));
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
                e.getPixelPosition().x >= bounds.pos.x - tileSize &&
                e.getPixelPosition().y >= bounds.pos.y - tileSize &&
                e.getPixelPosition().x <= bounds.pos.x + bounds.size.x &&
                e.getPixelPosition().y <= bounds.pos.y + bounds.size.y
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
