package de.schoolgame.network.packet;

public class WorldListPacket {
    public WorldListEntry[] list;

    public WorldListPacket() {}

    public WorldListPacket(WorldListEntry[] list) {
        this.list = list;
    }

    public static class WorldListEntry {
        public int id;
        public String name;
        public String creator;

        public WorldListEntry() {}

        public WorldListEntry(int id, String name, String creator) {
            this.id = id;
            this.name = name;
            this.creator = creator;
        }
    }
}
