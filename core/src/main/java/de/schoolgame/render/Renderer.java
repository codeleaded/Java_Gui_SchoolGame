package de.schoolgame.render;

import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.state.GameState;

public class Renderer implements IRenderer {
    WorldRenderer worldRenderer;
    ImGuiRenderer imGuiRenderer;
    Camera camera;

    public Renderer() {
        imGuiRenderer = new ImGuiRenderer();
        worldRenderer = new WorldRenderer();
        camera = new Camera();
    }

    public void render() {
        var color = GameState.INSTANCE.bg_color;
        ScreenUtils.clear(color[0], color[1], color[2], 1f);

        camera.update();
        worldRenderer.setView(camera);
        worldRenderer.render();

        if (GameState.INSTANCE.debug) {
            imGuiRenderer.render();
        }
    }

    public void dispose() {
        worldRenderer.dispose();
        imGuiRenderer.dispose();
    }
}
