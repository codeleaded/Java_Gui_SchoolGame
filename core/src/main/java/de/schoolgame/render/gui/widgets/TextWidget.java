package de.schoolgame.render.gui.widgets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Widget;
import de.schoolgame.render.texture.Font;
import de.schoolgame.state.GameState;

import java.util.function.Supplier;

public class TextWidget extends Widget {
    final int scale;
    final Supplier<String> text;

    public TextWidget(Vec2i pos, String text, int scale) {
        this(pos, () -> text, scale);
    }

    public TextWidget(Vec2i pos, Supplier<String> text, int scale) {
        super(pos, new Vec2i());
        this.scale = scale;
        this.text = text;

        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);

        rect.size = new Vec2i(font.getWidth(text.get(), scale), font.getHeight(scale));
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);

        sb.begin();
        font.draw(sb, text.get(), rect.pos, scale);
        sb.end();
    }

    @Override
    public boolean onClick() {
        return false;
    }

    @Override
    public void dispose() {

    }
}
