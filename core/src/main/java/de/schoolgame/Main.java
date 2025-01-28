package de.schoolgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.schoolgame.render.ImGuiRenderer;
import de.schoolgame.render.Renderer;
import de.schoolgame.state.GameState;

public class Main extends ApplicationAdapter {
    Renderer renderer;
    ImGuiRenderer imGuiRenderer;
    GameState gameState;

    @Override
    public void create() {
        Gdx.app.log("DEBUG", "Hello World!");
        renderer = new Renderer();
        gameState = new GameState();
        imGuiRenderer = new ImGuiRenderer();
    }

    @Override
    public void render() {
        renderer.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            GameState.INSTANCE.debug = !GameState.INSTANCE.debug;
            Gdx.app.log("DEBUG", GameState.INSTANCE.debug ? "DEBUG" : "DEBUG OFF");
        }
        if (GameState.INSTANCE.debug) {
            imGuiRenderer.render();
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
        imGuiRenderer.dispose();
    }
}
