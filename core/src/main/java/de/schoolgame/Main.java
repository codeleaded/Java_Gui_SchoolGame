package de.schoolgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.schoolgame.render.Renderer;
import de.schoolgame.state.GameInputProcessor;
import de.schoolgame.state.GameState;

public class Main extends ApplicationAdapter {
    Renderer renderer;
    GameState gameState;
    GameInputProcessor inputProcessor;

    @Override
    public void create() {
        Gdx.app.log("INFO", "Press \"L\" to open/close ImGui!");
        renderer = new Renderer();
        gameState = new GameState();
        Gdx.input.setInputProcessor(inputProcessor = new GameInputProcessor());
    }

    @Override
    public void render() {
        renderer.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            GameState.INSTANCE.debug = !GameState.INSTANCE.debug;
            Gdx.app.log("DEBUG", GameState.INSTANCE.debug ? "ImGui enabled" : "ImGui disabled");
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
