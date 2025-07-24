package de.schoolgame.render.gui.screens;

import com.badlogic.gdx.Gdx;
import de.schoolgame.network.packet.WorldListPacket;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.Sound;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.widgets.ButtonWidget;
import de.schoolgame.render.gui.widgets.RectangleWidget;
import de.schoolgame.render.gui.widgets.TextWidget;
import de.schoolgame.render.renderer.Renderer;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.Save;

public class WorldSelectScreen extends Screen {
    public WorldListPacket.WorldListEntry[] list;
    public float scroll = 0f;

    public WorldSelectScreen() {
        GameState.INSTANCE.server.sendPacket(new WorldListPacket(), true);
        refresh();
    }

    public void refresh() {
        int spacing = 10;
        Vec2i entrySize = new Vec2i(camera.viewSize.x - (spacing * 2), 32);
        final Vec2i buttonSize = new Vec2i(64, 64);

        int font_size = 3;
        int font_height = font_size * 7;
        int campaignHeight = camera.viewSize.y - (font_height + spacing * 2 + buttonSize.y);
        if (list == null) {
            this.scroll = -((float) campaignHeight / 2);
        } else {
            int listHeight = list.length * (entrySize.y + (spacing / 2));
            int maxOffset = -camera.viewSize.y + campaignHeight + listHeight - (camera.viewSize.y / 2) + spacing;
            if (maxOffset < 0) maxOffset = 0;
            this.scroll = Math.clamp(this.scroll, 0f, maxOffset);
        }
        widgets.clear();


        int x = spacing;
        int y = camera.viewSize.y + ((int) scroll);

        y -= font_height + spacing;

        widgets.add(new TextWidget(new Vec2i(x, y), "Kamapagne:", font_size));

        y -= spacing + buttonSize.y;
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

                Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/tap/tap", Sound.class);
                sound.play();
            }));
            x += buttonSize.x + spacing;
        }

        addBackButton();
        if (list == null) return;

        y = y + ((int) scroll) - 3 - (spacing * 2) ;
        x = spacing;

        widgets.add(new RectangleWidget(new Vec2i(0, 0), new Vec2i(camera.viewSize.x, y), Renderer.GUI_BG));
        widgets.add(new RectangleWidget(new Vec2i(x, y), new Vec2i(camera.viewSize.x - 2 * spacing, 3)));

        y -= font_height + spacing;
        widgets.add(new TextWidget(new Vec2i(x, y), "Welten von anderen Spielern:", font_size));

        for (WorldListPacket.WorldListEntry entry : list) {
            y -= (spacing / 2) + entrySize.y;
            widgets.add(new ButtonWidget(new Vec2i(spacing, y), entrySize, () -> entry.name,
                () -> GameState.INSTANCE.worldManager.download(entry.name), 3, new Vec2i(3, 3)));
        }
    }
}
