package de.schoolgame.render.gui.screens;

import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.TextWidget;
import de.schoolgame.state.GameState;

public class HudScreen extends Screen {
    public HudScreen() {
        int font_size = 2;

        int x = 10;
        int y = camera.viewSize.y - 10 - (7 * font_size);

        widgets.add(new TextWidget(new Vec2i(x, y), () -> "Coins: " + GameState.INSTANCE.player.getCoins(), font_size));
        widgets.add(new TextWidget(new Vec2i(x, y - (7 * font_size)), () -> "Score: " + GameState.INSTANCE.player.getScore(), font_size));
    }
}
