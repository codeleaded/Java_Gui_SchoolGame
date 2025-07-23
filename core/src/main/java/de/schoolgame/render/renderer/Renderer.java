package de.schoolgame.render.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import de.schoolgame.render.gui.screens.MessageScreen;
import de.schoolgame.state.GameState;

public class Renderer implements IRenderer {
    public static final Color GUI_BG = new Color(0.3f, 0.3f, 0.4f, 1f);
    public static final Color CREDITS_BG = new Color(0.6f, 0.6f, 0.8f, 1f);
    public static final Color WORLD_BG = new Color(0f, 0.5f, 1f, 1f);

    private final WorldRenderer worldRenderer;
    private final ImGuiRenderer imGuiRenderer;

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
                ScreenUtils.clear(GUI_BG);
                break;
            case CREDITS:
                ScreenUtils.clear(CREDITS_BG);
                break;
            case GAME:
                ScreenUtils.clear(WORLD_BG);
                worldRenderer.render();
                break;
            case WORLD_EDITOR:
            case DEBUG:
                ScreenUtils.clear(WORLD_BG);
                worldRenderer.render();
                imGuiRenderer.render();
                break;
        }

        if (state.messageRemaining > 0f) {
            state.messageRemaining -= Gdx.graphics.getDeltaTime();
            new MessageScreen().render();
            return;
        } else if (!state.message.isEmpty()) {
            state.message = "";
        }

        GameState.INSTANCE.screen.render();
    }

    public void dispose() {
        worldRenderer.dispose();
        imGuiRenderer.dispose();
    }
}
