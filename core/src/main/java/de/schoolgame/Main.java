package de.schoolgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.render.Renderer;
import de.schoolgame.state.GameState;

public class Main extends ApplicationAdapter {
    Renderer renderer;
    GameState gameState;

    @Override
    public void create() {
        renderer = new Renderer();
        gameState = new GameState();
    }

    @Override
    public void render() {
        renderer.render();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
