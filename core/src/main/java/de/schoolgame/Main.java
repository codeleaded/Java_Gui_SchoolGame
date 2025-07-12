package de.schoolgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import de.schoolgame.render.renderer.Renderer;
import de.schoolgame.state.GameInputProcessor;
import de.schoolgame.state.GameState;

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

        if (state.controllable()) {
            state.player.update();

            var entities = state.world.getEntities();
            for (int i = entities.size() - 1; i >= 0; i--) {
                var entity = entities.get(i);
                entity.update();
            }
        } else {
            state.camera.setGui();
        }

        renderer.render();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        GameState.INSTANCE.assetManager.dispose();
        Gdx.app.log("Main", "Disposed!");
    }
}
