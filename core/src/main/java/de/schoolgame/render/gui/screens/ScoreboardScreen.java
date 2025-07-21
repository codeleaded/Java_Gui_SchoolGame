package de.schoolgame.render.gui.screens;

import de.schoolgame.network.packet.ScoreboardPacket;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.TextWidget;
import de.schoolgame.state.GameState;

public class ScoreboardScreen extends Screen {
    public String[] names;
    public int[] scores;

    public ScoreboardScreen() {
        var state = GameState.INSTANCE;

        if (state.server.isConnected()) {
            state.server.sendPacket(new ScoreboardPacket(new String[0], new int[0]), true);
            widgets.add(new TextWidget(new Vec2i(10, 10), "Loading...", 2));
        } else {
            widgets.add(new TextWidget(new Vec2i(10, 10), "No Server connection!", 2));
        }
    }

    private void refresh() {
        widgets.clear();

        int spacing = (7 * 4);
        int y = camera.viewSize.y - 10 - (7 * 3);
        for (int i = 0; i < 10; i++) {
            widgets.add(new TextWidget(new Vec2i(10, y), (i + 1) + ". " + names[i] + " " + scores[i], 3));
            y -= spacing;
        }
    }

    public void setData(String[] names, int[] scores) {
        this.names = names;
        this.scores = scores;

        refresh();
    }
}
