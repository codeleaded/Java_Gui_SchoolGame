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
        var state = GameState.INSTANCE;

        if (state.state == GameState.GameStateType.MAIN_MENU) {
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        } else {
            ScreenUtils.clear(0f, 0.5f, 1f, 1f);
        }

        switch (state.state) {
            case GAME -> worldRenderer.render();
            case DEBUG -> {
                worldRenderer.render();
                debugRenderer.render();
            }
        }

        guiRenderer.render();
    }

    public void dispose() {
        worldRenderer.dispose();
        debugRenderer.dispose();
    }
}
