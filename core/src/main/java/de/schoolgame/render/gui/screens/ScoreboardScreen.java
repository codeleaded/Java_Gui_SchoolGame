package de.schoolgame.render.gui.screens;

import com.badlogic.gdx.graphics.Color;
import de.schoolgame.network.packet.ScoreboardPacket;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.RectangleWidget;
import de.schoolgame.render.gui.widgets.TextWidget;
import de.schoolgame.render.texture.Font;
import de.schoolgame.state.GameState;

public class ScoreboardScreen extends Screen {
    private long stateTime = System.currentTimeMillis();
    private String[] names;
    private int[] scores;

    public ScoreboardScreen() {
        var state = GameState.INSTANCE;

        if (state.server.isConnected()) {
            state.server.sendPacket(new ScoreboardPacket(new String[0], new int[0]), true);
            widgets.add(new TextWidget(new Vec2i(10, 10), "Loading...", 2));
        } else {
            widgets.add(new TextWidget(new Vec2i(10, 10), "No Server connection!", 2));
        }

        addBackButton();
    }

    private void refresh() {
        int font_scale = 3;
        int spacing = 7;
        int sideSpacing = 40;

        widgets.clear();

        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);
        Vec2i size = new Vec2i(camera.viewSize.x - (sideSpacing * 2), font.getHeight(font_scale) + spacing);

        int y = camera.viewSize.y - (spacing * 2) - (7 * 3);
        for (int i = 0; i < 10; i++) {
            Vec2i pos = new Vec2i(sideSpacing, y);

            Color color = switch (i) {
                case 0 -> Color.GOLD;
                case 1 -> Color.LIGHT_GRAY;
                case 2 -> Color.BROWN;
                default -> Color.DARK_GRAY;
            };

            widgets.add(new RectangleWidget(pos, size, color));

            pos = pos.add(4);

            widgets.add(new TextWidget(pos.cpy(), (i + 1) + ".", 3));
            widgets.add(new TextWidget(pos.add(font.getWidth("00.", font_scale), 0), names[i], 3));

            String score = "" + scores[i];
            int score_width = font.getWidth(score, font_scale);
            pos.x = camera.viewSize.x - spacing - sideSpacing - score_width;
            widgets.add(new TextWidget(pos, score, 3));
            y -= (spacing + size.y);
        }

        addBackButton();
    }

    @Override
    public void render() {
        super.render();

        long diff = System.currentTimeMillis() - stateTime;
        if (diff > 1000) {
            GameState.INSTANCE.server.sendPacket(new ScoreboardPacket(new String[0], new int[0]), true);
            stateTime = System.currentTimeMillis();
        }
    }

    public void setData(String[] names, int[] scores) {
        System.out.println("refresh");

        this.names = names;
        this.scores = scores;

        refresh();
    }
}
