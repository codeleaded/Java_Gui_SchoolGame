package de.schoolgame.render.renderer;

import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.state.GameState;

public class Renderer implements IRenderer {
    private final WorldRenderer worldRenderer;
    private final ImGuiRenderer imGuiRenderer;
    private final GuiRenderer guiRenderer;

    public Renderer() {
        imGuiRenderer = new ImGuiRenderer();
        worldRenderer = new WorldRenderer();
        guiRenderer = new GuiRenderer();
    }

    public void render() {
        var state = GameState.INSTANCE;

        switch (state.state) {
            case MAIN_MENU:
            case WORLD_SELECT:
                ScreenUtils.clear(0.3f, 0.3f, 0.4f, 1f);
                break;
            case GAME:
                ScreenUtils.clear(0f, 0.5f, 1f, 1f);
                worldRenderer.render();
                break;
            case WORLD_EDITOR:
            case DEBUG:
                ScreenUtils.clear(0f, 0.5f, 1f, 1f);
                worldRenderer.render();
                imGuiRenderer.render();
                break;
        }

        guiRenderer.render();
    }

    public void dispose() {
        worldRenderer.dispose();
        imGuiRenderer.dispose();
    }
}
