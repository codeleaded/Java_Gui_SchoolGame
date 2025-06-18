package de.schoolgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import de.schoolgame.render.renderer.Renderer;
import de.schoolgame.state.GameInputProcessor;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;

public class Main extends ApplicationAdapter {
    Renderer renderer;
    GameInputProcessor inputProcessor;

    @Override
    public void create() {
        Gdx.app.log("INFO", "Press \"L\" to open/close ImGui!");
        renderer = new Renderer();
        Gdx.input.setInputProcessor(inputProcessor = new GameInputProcessor());
    }

    @Override
    public void render() {
        var state = GameState.INSTANCE;

        inputProcessor.update();
        state.player.update();
        state.world.getEntities().forEach(Entity::update);

        renderer.render();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
