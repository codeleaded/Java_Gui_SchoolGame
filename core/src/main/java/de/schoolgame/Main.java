package de.schoolgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.schoolgame.render.Renderer;
import de.schoolgame.state.GameInputProcessor;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.Save;

public class Main extends ApplicationAdapter {
    Renderer renderer;
    GameInputProcessor inputProcessor;

    @Override
    public void create() {
        Gdx.app.log("INFO", "Press \"L\" to open/close ImGui!");
        renderer = new Renderer();
        Gdx.input.setInputProcessor(inputProcessor = new GameInputProcessor());
        GameState.INSTANCE.loadSave(Save.loadSave("worlds/save.dat"));
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
