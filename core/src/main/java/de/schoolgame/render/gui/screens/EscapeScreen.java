package de.schoolgame.render.gui.screens;

import com.badlogic.gdx.graphics.Color;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.RectangleWidget;
import de.schoolgame.render.gui.widgets.TextWidget;
import de.schoolgame.state.GameState;

public class EscapeScreen extends Screen {
    public EscapeScreen() {
        int font_size = 3;

        String text = "Fürs Menü nochmal drücken";

        TextWidget textWidget = new TextWidget(new Vec2i(), text, font_size);
        Vec2i pos = GameState.INSTANCE.camera.viewSize.sub(textWidget.getSize()).div(2);
        textWidget.setPos(pos);

        widgets.add(new RectangleWidget(pos.sub(10), textWidget.getSize().add(20), Color.LIGHT_GRAY));
        widgets.add(textWidget);
    }
}
