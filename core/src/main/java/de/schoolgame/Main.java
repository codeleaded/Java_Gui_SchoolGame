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

        // Für Debugging -> Clear Screen + Cursur to 0,0
        // System.out.print("\033[2J\033[H");
        // System.out.flush();

        inputProcessor.update();
        state.player.update();
        state.world.getEntities().forEach(Entity::update);

        renderer.render();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        GameState.INSTANCE.assetManager.dispose();
        Gdx.app.log("Main", "Disposed!");
    }
}
