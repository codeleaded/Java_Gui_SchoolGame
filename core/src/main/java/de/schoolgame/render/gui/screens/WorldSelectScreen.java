package de.schoolgame.render.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.ButtonWidget;
import de.schoolgame.render.gui.widgets.RectangleWidget;
import de.schoolgame.render.gui.widgets.TextWidget;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.Save;

public class WorldSelectScreen extends Screen {
    public WorldSelectScreen() {
        super();

        final int buttonSpacing = 10;

        final Vec2i buttonSize = new Vec2i(64, 64);
        final int spacing = buttonSize.x + buttonSpacing;

        final int font_size = 3;
        final int font_height = font_size * 7;

        int x = buttonSpacing;
        int y = camera.viewSize.y;

        y -= font_height + buttonSpacing;

        widgets.add(new TextWidget(new Vec2i(x, y), "Kampgne:", font_size));

        y -= buttonSpacing + buttonSize.y;
        for (int i = 0; i < 7; i++) {
            String text = i == 0 ? "T" : "" + i;
            int finalI = i;
            widgets.add(new ButtonWidget(new Vec2i(x, y), buttonSize, text, () -> {
                var state = GameState.INSTANCE;
                Gdx.app.log("WorldSelect", "Selected world: " + finalI);
                Save save = state.worldManager.get("world_" + finalI);
                state.loadSave(save);
                state.world.summonEntities();
                state.setState(GameState.GameStateType.GAME);

                Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/tap", Sound.class);
                sound.play(1.0f);
            }));
            x += spacing;
        }

        x = buttonSpacing;
        y -= buttonSpacing;
        widgets.add(new RectangleWidget(new Vec2i(x, y), new Vec2i(camera.viewSize.x - 2 * buttonSpacing, 3)));

        y -= font_height + buttonSpacing;
        widgets.add(new TextWidget(new Vec2i(x, y), "Welten von anderen Spielern:", font_size));
    }
}
