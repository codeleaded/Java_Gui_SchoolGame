package de.schoolgame.render.renderer;

import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.state.GameState;

public class Renderer implements IRenderer {
    private final WorldRenderer worldRenderer;
    private final DebugRenderer debugRenderer;
    private final GuiRenderer guiRenderer;

    public Renderer() {
        debugRenderer = new DebugRenderer();
        worldRenderer = new WorldRenderer();
        guiRenderer = new GuiRenderer();
    }

    public void render() {
        var camera = GameState.INSTANCE.camera;

        ScreenUtils.clear(0f, 0.5f, 1f, 1f);

        camera.update();
        camera.focusPlayer();
        worldRenderer.setView(camera);

        switch (GameState.INSTANCE.state) {
            case GAME -> worldRenderer.render();
            case DEBUG -> {
                worldRenderer.render();
                debugRenderer.render();
            }
            case MAIN_MENU -> guiRenderer.render();
        }
    }

    public void dispose() {
        worldRenderer.dispose();
        debugRenderer.dispose();
    }
}
