package de.schoolgame.render.gui.widgets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Recti;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Widget;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.state.GameState;

public class TextureButtonWidget extends Widget {
    private final Runnable onClick;
    private final TextureRegion texture;
    private boolean hovered = false;

    public TextureButtonWidget(Vec2i pos, Vec2i size, int textureIndex, Runnable onClick) {
        this(pos, size, GameState.INSTANCE.assetManager.get("gui/buttons/buttons", SpriteSheet.class)
                .getRegions()[textureIndex], onClick);
    }

    public TextureButtonWidget(Vec2i pos, Vec2i size, TextureRegion texture, Runnable onClick) {
        super(pos, size);
        this.onClick = onClick;
        this.texture = texture;
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin();

        Recti rect = this.rect.cpy();
        if (hovered && onClick != null) {
            rect.size = rect.size.sub(6, 6);
            rect.pos = rect.pos.add(3, 3);
        }
        batch.draw(texture, rect.pos.x, rect.pos.y, rect.size.x, rect.size.y);

        batch.end();
    }

    @Override
    public boolean onClick() {
        if (onClick == null) return false;
        onClick.run();
        return true;
    }

    @Override
    public boolean onHover(boolean hovered) {
        this.hovered = hovered;
        return true;
    }

    @Override
    public void dispose() {

    }
}
