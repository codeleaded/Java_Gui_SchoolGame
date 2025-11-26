package de.schoolgame.render.gui.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Widget;

public class RectangleWidget extends Widget {
    private final Color color;

    public RectangleWidget(Vec2i pos, Vec2i size) {
        super(pos, size);
        color = new Color(0x000000ff);
    }

    public RectangleWidget(Vec2i pos, Vec2i size, Color color) {
        super(pos, size);
        this.color = color;
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(rect.pos.x, rect.pos.y, rect.size.x, rect.size.y);
        sr.end();
    }

    @Override
    public void dispose() {

    }
}
