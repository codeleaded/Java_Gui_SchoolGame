package de.schoolgame.render.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.Camera;
import de.schoolgame.render.texture.Font;
import de.schoolgame.state.GameState;

public class GuiRenderer implements IRenderer {
    public static final int BUTTON_WIDTH = 300;
    public static final int BUTTON_HEIGHT = 64;
    public static final int BUTTON_SPACING = 10;

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

        if (GameState.INSTANCE.escapeFlag) {
            renderEscape();
            return;
        }

        switch (GameState.INSTANCE.state) {
            case MAIN_MENU -> renderMainMenu();
            case GAME -> renderHud();
            default -> {
            }
        }
    }

    private void renderEscape() {
        var state = GameState.INSTANCE;

        Font font = state.assetManager.get("gui/font/aseprite_font", Font.class);
        int font_size = 3;

        String text = "Fürs Menü nochmal drücken";
        Vec2i size = new Vec2i(font.getWidth(text, font_size), (7 * font_size));
        Vec2i pos = state.camera.viewSize.sub(size).div(2);

        batch.begin();
        font.draw(batch, text, pos.x, pos.y, font_size);
        batch.end();
    }

    private void renderHud() {
        var state = GameState.INSTANCE;

        Font font = state.assetManager.get("gui/font/aseprite_font", Font.class);
        int font_size = 2;

        int x = 10;
        int y = camera.viewSize.y - 10 - (7 * font_size);

        batch.begin();
        font.draw(batch, "Coins: " + state.player.getCoins(), x, y, font_size);
        batch.end();
    }

    private void renderMainMenu() {
        int travel = GuiRenderer.BUTTON_HEIGHT + GuiRenderer.BUTTON_SPACING;
        int x = (camera.viewSize.x - BUTTON_WIDTH) / 2;
        int y = BUTTON_SPACING;

        renderButton(x, y, "Beenden", BUTTON_WIDTH);
        y += travel;
        renderButton(x, y, "Create", BUTTON_WIDTH);
        y += travel;
        renderButton(x, y, "Start", BUTTON_WIDTH);
    }

    public void renderButton(int x, int y, String text, int width) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Color highlight = new Color(0xffffffff);
        Color light = new Color(0xb4b4b4ff);
        Color dark = new Color(0x4d4d4dff);

        shapeRenderer.setColor(light);
        shapeRenderer.rect(x, y, width, BUTTON_HEIGHT - 3);

        shapeRenderer.setColor(highlight);
        shapeRenderer.rect(x, y + BUTTON_HEIGHT - 3, width, 3);
        shapeRenderer.rect(x + 3, y + 3, width - 6, 3);

        shapeRenderer.setColor(dark);
        shapeRenderer.rect(x + 3, y + 6, width - 6, BUTTON_HEIGHT - 12);
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
