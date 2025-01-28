package de.schoolgame.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Renderer implements IRenderer {
    private final SpriteBatch batch;
    private final Texture image;

    public Renderer() {
        batch = new SpriteBatch();
        image = new Texture("coin.png");
    }

    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 0, 0);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
