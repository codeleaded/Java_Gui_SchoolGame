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
        "Michael", "Lara", "Yanis"
    };

    private static final String[] subtitles = {
        null,
        "Grafik-Engine", "Physik-Engine",
        null,
        "Allgemein", "Charaktere", "Tilesets"
    };

    public  CreditScreen() {
        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);

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
        for (int i = 0; i < titles.length; i++) {
            Vec2i pos = new Vec2i(spacing, y);
            if (subtitles[i] == null) {
                pos.y -= titleSpacing;

                widgets.add(new TextWidget(pos, titles[i], titleFontScale));
                y -= titleFontHeight + titleSpacing;
            } else {
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
