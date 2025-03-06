package de.schoolgame.render;

import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.state.GameState;

public class Renderer implements IRenderer {
    WorldRenderer worldRenderer;
    ImGuiRenderer imGuiRenderer;

    public Renderer() {
        imGuiRenderer = new ImGuiRenderer();
        worldRenderer = new WorldRenderer();
    }

    public void render() {
        var camera = GameState.INSTANCE.camera;

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();
        worldRenderer.setView(camera);
        worldRenderer.render();

        if (GameState.INSTANCE.debug.enabled) {
            imGuiRenderer.render();
        }
    }

    public void dispose() {
        worldRenderer.dispose();
        imGuiRenderer.dispose();
    }
}
