package de.schoolgame.render.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.render.gui.screens.MessageScreen;
import de.schoolgame.state.GameState;

public class Renderer implements IRenderer {
    public static final Color GUI_BG = new Color(0.3f, 0.3f, 0.4f, 1f);
    public static final Color CREDITS_BG = new Color(0.6f, 0.6f, 0.8f, 1f);
    public static final Color WORLD_BG = new Color(0x19b7ffff);
    public static final Color WORLD_BG_DARK = new Color(0x252760ff);

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
                if ("world_6".equals(GameState.INSTANCE.worldName)) {
                    ScreenUtils.clear(WORLD_BG_DARK);
                } else {
                    ScreenUtils.clear(WORLD_BG);
                }
                worldRenderer.render();
                break;
            case WORLD_EDITOR:
            case DEBUG:
                if ("world_6".equals(GameState.INSTANCE.worldName)) {
                    ScreenUtils.clear(WORLD_BG_DARK);
                } else {
                    ScreenUtils.clear(WORLD_BG);
                }
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
