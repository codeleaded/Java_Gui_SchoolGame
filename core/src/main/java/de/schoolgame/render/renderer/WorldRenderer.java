package de.schoolgame.render.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.schoolgame.primitives.Rectf;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.Camera;
import de.schoolgame.state.GameState;
import de.schoolgame.world.World;

public class WorldRenderer implements IRenderer {
    private final Batch batch;
    private Rectf bounds;

    public WorldRenderer() {
        this.batch = new SpriteBatch();
        this.bounds = new Rectf();
    }

    public void setView(Camera camera) {
        var viewSize = camera.viewSize.toVec2f().mul(camera.zoom);
        var pos = camera.position.toVec2f().sub(viewSize.div(2));
        this.bounds = new Rectf(pos, viewSize);

        batch.setProjectionMatrix(camera.viewProjectionMatrix);
    }

    @Override
    public void render() {
        var camera = GameState.INSTANCE.camera;
        camera.clampZoom();
        camera.update();
        camera.focusPlayer();
        setView(camera);

        final World world = GameState.INSTANCE.world;
        final int tileSize = world.getTileSize();

        Vec2i start = bounds.pos.toVec2i()
            .div(tileSize)
            .max(new Vec2i(0, 0));
        Vec2i end = bounds.end().toVec2i()
            .div(tileSize).add(new Vec2i(1, 1))
            .min(world.getSize());

        Texture egg = GameState.INSTANCE.assetManager.get("tiles/egg/egg", Texture.class);

        batch.begin();
        float y = start.y * tileSize;
        for (int row = start.y; row < end.y; row++) {
            float x = start.x * tileSize;
            for (int col = start.x; col < end.x; col++) {
                var pos = new Vec2i(col, row);
                var worldObject = world.at(pos);
                var tile = worldObject.getTile();

                if (tile == null) {
                    if (worldObject.isEntity() && GameState.INSTANCE.state == GameState.GameStateType.WORLD_EDITOR) {
                        batch.setColor(worldObject.getEntityColor());
                        batch.draw(egg, x, y, tileSize, tileSize);
                        batch.setColor(Color.WHITE);
                    }

                    x += tileSize;
                    continue;
                }

                tile.render(batch, new Vec2f(x, y), pos);

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
