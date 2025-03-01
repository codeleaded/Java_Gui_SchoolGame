package de.schoolgame.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.state.GameState;

public class Renderer implements IRenderer {
    private final SpriteBatch batch;
    private final Animation coin;
    private float stateTime;

    public Renderer() {
        batch = new SpriteBatch();

        Texture[] textures = new Texture[50];
        for (int i = 1; i < 51; i++) {
            textures[i - 1] = new Texture("coin/" + i + ".png");
        }
        coin = new Animation(GameState.INSTANCE.animation_delay[0], textures);

        stateTime = 0f;
    }

    public void render() {
        coin.setDelay(GameState.INSTANCE.animation_delay[0]);

        var color = GameState.INSTANCE.bg_color;
        ScreenUtils.clear(color[0], color[1], color[2], 1f);

        stateTime += Gdx.graphics.getDeltaTime();

        batch.begin();
        batch.draw(coin.currentFrame(stateTime), 0, 0);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        coin.dispose();
    }
}
