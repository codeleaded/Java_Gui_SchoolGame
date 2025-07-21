package de.schoolgame.render.gui.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Widget;
import de.schoolgame.render.texture.Font;
import de.schoolgame.state.GameState;

public class ButtonWidget extends Widget {
    private final String text;
    private final Runnable onClick;

    public ButtonWidget(Vec2i pos, Vec2i size, String text, Runnable onClick) {
        super(pos, size);
        this.text = text;
        this.onClick = onClick;
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Color highlight = new Color(0xffffffff);
        Color light = new Color(0xb4b4b4ff);
        Color dark = new Color(0x4d4d4dff);

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
        int textX = rect.size.x - font.getWidth(text, 8);
        textX /= 2;

        font.draw(batch, text, rect.pos.cpy().add(textX, 8), 8);
        batch.end();
    }

    public String getText() {
        return text;
    }

    @Override
    public void onClick() {
        onClick.run();
    }

    @Override
    public void dispose() {

    }
}
