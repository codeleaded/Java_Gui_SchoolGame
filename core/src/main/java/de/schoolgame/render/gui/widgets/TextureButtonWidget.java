package de.schoolgame.render.gui.widgets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Widget;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.state.GameState;

public class TextureButtonWidget extends Widget {
    private final Runnable onClick;
    private final int textureIndex;

    public TextureButtonWidget(Vec2i pos, Vec2i size, int textureIndex, Runnable onClick) {
        super(pos, size);
        this.onClick = onClick;
        this.textureIndex = textureIndex;
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        var state = GameState.INSTANCE;
        TextureRegion region = state.assetManager.get("gui/buttons/buttons", SpriteSheet.class)
            .getRegions()[textureIndex];

        batch.begin();

        batch.draw(region, rect.pos.x, rect.pos.y, rect.size.x, rect.size.y);

        batch.end();
    }

    @Override
    public void onClick() {
        onClick.run();
    }

    @Override
    public void dispose() {

    }
}
