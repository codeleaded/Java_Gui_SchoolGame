package de.schoolgame.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.state.GameState;

public class Renderer implements IRenderer {
    private final SpriteBatch batch;
    private final Texture image;

    public Renderer() {
        batch = new SpriteBatch();
        image = new Texture("coin.png");
    }

    public void render() {
        var color = GameState.INSTANCE.bg_color;
        ScreenUtils.clear(color[0], color[1], color[2], 1f);
        batch.begin();
        batch.draw(image, 0, 0);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
