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
        Vec2i bigButtonSize = new Vec2i(180, 32).mul(2);
        Vec2i squareButtonSize = new Vec2i(32, 32).mul(2);
        final int buttonSpacing = 10;

        int x = (camera.viewSize.x - bigButtonSize.x) / 2;
        int y = buttonSpacing;

        if (GameState.INSTANCE.server.isConnected()) {
            widgets.add(new TextureButtonWidget(new Vec2i(x, y), squareButtonSize, 3, () -> GameState.INSTANCE.setState(GameState.GameStateType.SCOREBOARD)));
            x += squareButtonSize.x + buttonSpacing;
        }

        widgets.add(new TextureButtonWidget(new Vec2i(x, y), squareButtonSize, 5, () -> GameState.INSTANCE.setState(GameState.GameStateType.CREDITS)));

        x = ((camera.viewSize.x - bigButtonSize.x) / 2) + (bigButtonSize.x - squareButtonSize.x);

        widgets.add(new TextureButtonWidget(new Vec2i(x, y), squareButtonSize, 2, () -> Gdx.app.exit()));

        y += squareButtonSize.y + buttonSpacing;
        x = (camera.viewSize.x - bigButtonSize.x) / 2;

        widgets.add(new TextureButtonWidget(new Vec2i(x, y), bigButtonSize, 1, () -> {
            var state = GameState.INSTANCE;
            state.world = new World();
            state.player = new PlayerEntity(new Vec2f(1.0f,1.0f));
            state.setState(GameState.GameStateType.WORLD_EDITOR);
        }));

        y += bigButtonSize.y + buttonSpacing;

        widgets.add(new TextureButtonWidget(new Vec2i(x, y), bigButtonSize, 0, () -> {
            var state = GameState.INSTANCE;
            state.setState(GameState.GameStateType.WORLD_SELECT);
        }));
    }
}
