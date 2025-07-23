package de.schoolgame.render.gui.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Widget;
import de.schoolgame.render.texture.Font;
import de.schoolgame.state.GameState;

import java.util.function.Supplier;

public class ButtonWidget extends Widget {
    private final Supplier<String> text;
    private final Runnable onClick;
    private final int fontSize;
    private final Color highlight = new Color(0xffffffff);
    private final Color light = new Color(0xb4b4b4ff);
    private final Color dark = new Color(0x4d4d4dff);

    public ButtonWidget(Vec2i pos, Vec2i size, String text, Runnable onClick) {
        this(pos, size, () -> text, onClick);
    }

    public ButtonWidget(Vec2i pos, Vec2i size, Supplier<String> text, Runnable onClick) {
        this(pos, size, text, onClick, 8);
    }

    public ButtonWidget(Vec2i pos, Vec2i size, Supplier<String> text, Runnable onClick, int fontSize) {
        super(pos, size);
        this.text = text;
        this.onClick = onClick;
        this.fontSize = fontSize;
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        String text = this.text.get();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(light);
        shapeRenderer.rect(rect.pos.x, rect.pos.y, rect.size.x, rect.size.y - 3);

        shapeRenderer.setColor(highlight);
        shapeRenderer.rect(rect.pos.x, rect.pos.y + rect.size.y - 3, rect.size.x, 3);
        shapeRenderer.rect(rect.pos.x + 3, rect.pos.y + 3, rect.size.x - 6, 3);

        shapeRenderer.setColor(dark);
        shapeRenderer.rect(rect.pos.x + 3, rect.pos.y + 6, rect.size.x - 6, rect.size.y - 12);
        shapeRenderer.end();

        batch.begin();

        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);
        int textX = rect.size.x - font.getWidth(text, fontSize);
        textX /= 2;

        font.draw(batch, text, rect.pos.add(textX, 0), fontSize);
        batch.end();
    }

    @Override
    public boolean onClick() {
        if (onClick == null) return false;
        onClick.run();
        return true;
    }

    @Override
    public void dispose() {

    }
}
