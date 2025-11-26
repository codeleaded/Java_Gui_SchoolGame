package de.schoolgame.network.packet;

import com.esotericsoftware.kryonet.Connection;
import de.schoolgame.network.Packet;
import de.schoolgame.render.gui.screens.WorldSelectScreen;
import de.schoolgame.state.GameState;

public class WorldListPacket implements Packet {
    public WorldListEntry[] list;

    public WorldListPacket() {}

    public WorldListPacket(WorldListEntry[] list) {
        this.list = list;
    }

    public static class WorldListEntry {
        public String name;
        public String creator;

        public WorldListEntry() {}

        public WorldListEntry(String name, String creator) {
            this.name = name;
            this.creator = creator;
        }
    }

    @Override
    public void handle(Connection connection) {
        var state = GameState.INSTANCE;

        if (state.screen instanceof WorldSelectScreen s) {
            s.list = list;
            s.refresh();
        }
    }
}
