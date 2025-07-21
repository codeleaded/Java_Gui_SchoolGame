package de.schoolgame.render.renderer;

import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.render.gui.screens.EscapeScreen;
import de.schoolgame.state.GameState;

public class Renderer implements IRenderer {
    private final WorldRenderer worldRenderer;
    private final ImGuiRenderer imGuiRenderer;

    private final static EscapeScreen escapeScreen = new EscapeScreen();

    public Renderer() {
        imGuiRenderer = new ImGuiRenderer();
        worldRenderer = new WorldRenderer();
    }

    public void render() {
        var state = GameState.INSTANCE;

        switch (state.getState()) {
            case MAIN_MENU:
            case WORLD_SELECT:
            case CHARACTER_SELECT:
            case SCOREBOARD:
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

        if (GameState.INSTANCE.escapeFlag) {
            escapeScreen.render();
            return;
        }

        GameState.INSTANCE.screen.render();
    }

    public void dispose() {
        worldRenderer.dispose();
        imGuiRenderer.dispose();
    }
}
