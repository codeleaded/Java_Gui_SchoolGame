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
        var state = GameState.INSTANCE;
        state.player.update();

        renderer.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            state.debug.enabled = !state.debug.enabled;
            Gdx.app.log("DEBUG", state.debug.enabled ? "ImGui enabled" : "ImGui disabled");
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
