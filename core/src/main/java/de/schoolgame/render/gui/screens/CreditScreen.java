package de.schoolgame.render.gui.screens;

import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.TextWidget;
import de.schoolgame.render.texture.Font;
import de.schoolgame.state.GameState;

public class CreditScreen extends Screen {
    private static final String[] titles = {
        "Programmierer",
        "David", "Alex",
        "Grafik",
        "Yanis", "David", "Lara", "Michael"
    };

    private static final String[] subtitles = {
        null,
        "Grafik-Engine, Server", "Physik-Engine, Map Design",
        null,
        "Map Design, Tilesets", "UI", "Charaktere", "Git-Terror (bug fix)"
    };

    public  CreditScreen() {
        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);

        int shift = camera.viewSize.x / 2;

        int titleFontScale = 4;
        int titleSpacing = 20;
        int titleFontHeight = font.getHeight(titleFontScale);

        int fontScale = 3;
        int spacing = 10;
        int fontHeight = font.getHeight(fontScale);

        int subFontScale = 2;
        int subSpacing = 5;
        int subFontHeight = font.getHeight(subFontScale);

        int y = camera.viewSize.y - fontHeight;
        int x = spacing - shift;
        for (int i = 0; i < titles.length; i++) {
            if (subtitles[i] == null) {
                x += shift;
                y = camera.viewSize.y - fontHeight;

                Vec2i pos = new Vec2i(x, y - titleSpacing);
                widgets.add(new TextWidget(pos, titles[i], titleFontScale));
                y -= titleFontHeight + titleSpacing;
            } else {
                Vec2i pos = new Vec2i(x, y);
                pos.x += spacing * 2;

                widgets.add(new TextWidget(pos, titles[i], fontScale));
                y -= fontHeight+ spacing;

                widgets.add(new TextWidget(pos.sub(0, subSpacing + subFontHeight), subtitles[i], subFontScale));
                y -= subFontHeight + subSpacing;
            }
        }

        addBackButton();
    }
}
