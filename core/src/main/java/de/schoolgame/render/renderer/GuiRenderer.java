package de.schoolgame.render.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.render.Camera;
import de.schoolgame.render.texture.Font;
import de.schoolgame.state.GameState;

public class GuiRenderer implements IRenderer {
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Camera camera;

    public GuiRenderer() {
        batch = new SpriteBatch();
        camera = new Camera();
    }

    @Override
    public void render() {
        camera.update();
        batch.setProjectionMatrix(camera.viewProjectionMatrix);
        shapeRenderer.setProjectionMatrix(camera.viewProjectionMatrix);

        switch (GameState.INSTANCE.state) {
            case MAIN_MENU -> renderMainMenu();
            case OPTIONS -> throw new IllegalStateException("Options are not yet implemented!");
            default -> {
            }
        }
    }

    private void renderMainMenu() {
        int buttonWidth = 410;
        int x = camera.viewSize.x - buttonWidth;
        x /= 2;
        int y = camera.viewSize.y / 2;
        y -= 64 + 10;
        renderButton(x, y, "Multiplayer", buttonWidth);
        y -= 64 + 10;
        renderButton(x, y, "Singleplayer", buttonWidth);
    }

    public void renderButton(int x, int y, String text, int width) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Color highlight = new Color(0xffffffff);
        Color light = new Color(0xb4b4b4ff);
        Color dark = new Color(0x4d4d4dff);

        shapeRenderer.setColor(light);
        shapeRenderer.rect(x, y, width, 64 - 3);

        shapeRenderer.setColor(highlight);
        shapeRenderer.rect(x, y + 64 - 3, width, 3);
        shapeRenderer.rect(x + 3, y + 3, width - 6, 3);

        shapeRenderer.setColor(dark);
        shapeRenderer.rect(x + 3, y + 6, width - 6, 52);
        shapeRenderer.end();

        batch.begin();
        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);
        int textX = width - font.getWidth(text, 8);
        textX /= 2;

        font.draw(batch, text, x + textX, y + 8, 8);
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
