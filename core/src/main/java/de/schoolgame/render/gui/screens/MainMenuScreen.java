package de.schoolgame.render.gui.screens;

import com.badlogic.gdx.Gdx;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.TextureButtonWidget;
import de.schoolgame.state.GameState;
import de.schoolgame.world.World;
import de.schoolgame.world.entities.PlayerEntity;

public class MainMenuScreen extends Screen {
    public MainMenuScreen() {
        super();

        Vec2i bigButtonSize = new Vec2i(180, 32).mul(2);
        Vec2i squareButtonSize = new Vec2i(32, 32).mul(2);
        final int buttonSpacing = 10;

        int x = (camera.viewSize.x - bigButtonSize.x) / 2;
        int y = buttonSpacing;

        widgets.add(new TextureButtonWidget(new Vec2i(x, y), squareButtonSize, 3, () -> {
            var state = GameState.INSTANCE;
            state.screen = new ScoreboardScreen();
        }));

        x += bigButtonSize.x - squareButtonSize.x;

        widgets.add(new TextureButtonWidget(new Vec2i(x, y), squareButtonSize, 2, () -> Gdx.app.exit()));

        y += squareButtonSize.y + buttonSpacing;
        x = (camera.viewSize.x - bigButtonSize.x) / 2;

        widgets.add(new TextureButtonWidget(new Vec2i(x, y), bigButtonSize, 1, () -> {
            var state = GameState.INSTANCE;
            state.world = new World();
            state.player = new PlayerEntity(new Vec2f(1.0f,1.0f));
            state.player.setGodmode(true);
            state.state = GameState.GameStateType.WORLD_EDITOR;
            state.screen = new HudScreen();
        }));

        y += bigButtonSize.y + buttonSpacing;

        widgets.add(new TextureButtonWidget(new Vec2i(x, y), bigButtonSize, 0, () -> {
            var state = GameState.INSTANCE;
            state.state = GameState.GameStateType.WORLD_SELECT;
            state.screen = new WorldSelectScreen();
        }));
    }
}
