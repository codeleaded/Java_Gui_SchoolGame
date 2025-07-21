package de.schoolgame.render.gui.screens;

import com.badlogic.gdx.Gdx;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.ButtonWidget;
import de.schoolgame.state.GameState;
import de.schoolgame.world.World;
import de.schoolgame.world.entities.PlayerEntity;

public class MainMenuScreen extends Screen {
    public MainMenuScreen() {
        super();

        Vec2i buttonSize = new Vec2i(300, 64);
        final int buttonSpacing = 10;

        int travel = buttonSize.y + buttonSpacing;
        int x = (camera.viewSize.x - buttonSize.x) / 2;
        int y = buttonSpacing;
        widgets.add(new ButtonWidget(new Vec2i(x, y), buttonSize, "Beenden", () -> Gdx.app.exit()));
        y += travel;
        widgets.add(new ButtonWidget(new Vec2i(x, y), buttonSize, "Bauen", () -> {
            var state = GameState.INSTANCE;
            state.world = new World();
            state.player = new PlayerEntity(new Vec2f(1.0f,1.0f));
            state.player.setGodmode(true);
            state.state = GameState.GameStateType.WORLD_EDITOR;
            state.screen = new HudScreen();
        }));
        y += travel;
        widgets.add(new ButtonWidget(new Vec2i(x, y), buttonSize, "Start", () -> {
            var state = GameState.INSTANCE;
            state.state = GameState.GameStateType.WORLD_SELECT;
            state.screen = new WorldSelectScreen();
        }));
    }
}
