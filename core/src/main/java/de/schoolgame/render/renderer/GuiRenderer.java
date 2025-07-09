package de.schoolgame.render.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Vec2i;
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
        camera.setGui();
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
            case WORLD_SELECT -> renderWorldSelect();
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
        Vec2i buttonSize = new Vec2i(300, 64);
        final int buttonSpacing = 10;

        int travel = buttonSize.y + buttonSpacing;
        int x = (camera.viewSize.x - buttonSize.x) / 2;
        int y = buttonSpacing;

        renderButton(x, y, "Beenden", buttonSize);
        y += travel;
        renderButton(x, y, "Bauen", buttonSize);
        y += travel;
        renderButton(x, y, "Start", buttonSize);
    }

    private void renderWorldSelect() {
        final int buttonSpacing = 10;

        final Vec2i campaignButtonSize = new Vec2i(64, 64);
        final int campaignSpacing = campaignButtonSize.x + buttonSpacing;

        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);
        final int font_size = 3;
        final int font_height = font_size * 7;

        int x = buttonSpacing;
        int y = camera.viewSize.y;

        y -= font_height + buttonSpacing;
        batch.begin();
        font.draw(batch, "Kampagne:", x, y, font_size);
        batch.end();

        y -= buttonSpacing + campaignButtonSize.y;
        for (int i = 0; i < 7; i++) {
            String text = i == 0 ? "T" : "" + i;
            renderButton(x, y, text, campaignButtonSize);
            x += campaignSpacing;
        }

        x = buttonSpacing;
        y -= buttonSpacing;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0x000000ff));
        shapeRenderer.rect(x, y, camera.viewSize.x - 2 * buttonSpacing, 3);
        shapeRenderer.end();

        y -= font_height + buttonSpacing;
        batch.begin();
        font.draw(batch, "Welten von anderen Spielern:", x, y, font_size);
        batch.end();

        //TODO Server worlds
    }

    public void renderButton(int x, int y, String text, Vec2i size) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Color highlight = new Color(0xffffffff);
        Color light = new Color(0xb4b4b4ff);
        Color dark = new Color(0x4d4d4dff);

        shapeRenderer.setColor(light);
        shapeRenderer.rect(x, y, size.x, size.y - 3);

        shapeRenderer.setColor(highlight);
        shapeRenderer.rect(x, y + size.y - 3, size.x, 3);
        shapeRenderer.rect(x + 3, y + 3, size.x - 6, 3);

        shapeRenderer.setColor(dark);
        shapeRenderer.rect(x + 3, y + 6, size.x - 6, size.y - 12);
        shapeRenderer.end();

        batch.begin();
        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);
        int textX = size.x - font.getWidth(text, 8);
        textX /= 2;

        font.draw(batch, text, x + textX, y + 8, 8);
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
