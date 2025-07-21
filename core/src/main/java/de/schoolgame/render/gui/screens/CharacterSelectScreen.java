package de.schoolgame.render.gui.screens;

import com.badlogic.gdx.graphics.Color;
import de.schoolgame.network.packet.LoginPacket;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.ButtonWidget;
import de.schoolgame.render.gui.widgets.RectangleWidget;
import de.schoolgame.render.gui.widgets.TextWidget;
import de.schoolgame.render.gui.widgets.TextureButtonWidget;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.state.GameState;

public class CharacterSelectScreen extends Screen {
    public RectangleWidget indicator;

    public CharacterSelectScreen() {
        int spacing = 10;

        TextWidget text = new TextWidget(new Vec2i(spacing, spacing), "Wähle deinen Character und Benutzernamen", 3);
        text.setPos(new Vec2i(spacing, camera.viewSize.y - text.getSize().y - spacing));
        widgets.add(text);

        int y = text.getPos().y - spacing;

        Vec2i size = new Vec2i( 32*2, 58*2);
        int start = camera.viewSize.x - (spacing + ((spacing + size.x) * 7));
        start /= 2;

        indicator = new RectangleWidget(new Vec2i(start, y - size.y - spacing - 3), size.add(0, 6), Color.GREEN);
        widgets.add(indicator);

        for (int i = 1; i < 8; i++) {
            Vec2i pos = new Vec2i(start + ((spacing + size.x) * (i - 1)), y - size.y - spacing);

            SpriteSheet texture = GameState.INSTANCE.assetManager.get("entities/player/player_" + i, SpriteSheet.class);

            int finalI = i;
            widgets.add(new TextureButtonWidget(pos, size, texture.getRegions()[0], () -> {
                if (GameState.INSTANCE.screen instanceof CharacterSelectScreen s) {
                    s.indicator.setPos(pos.sub(0, 3));
                    GameState.INSTANCE.playerStyle = finalI;
                }
            }));
        }

        Vec2i pos = new Vec2i(spacing, spacing);
        size = new Vec2i(camera.viewSize.x - (spacing * 3) - 64, 64);
        widgets.add(new ButtonWidget(pos, size, () -> GameState.INSTANCE.username, () -> {}));

        widgets.add(new TextureButtonWidget(pos.add(size.x + spacing, 0), new Vec2i(64, 64), 4, () -> {
            var state = GameState.INSTANCE;
            state.server.sendPacket(new LoginPacket(state.username, null, state.playerStyle), true);

            state.setState(GameState.GameStateType.MAIN_MENU);
        }));
    }
}
