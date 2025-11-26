package de.schoolgame.network;

import de.schoolgame.network.packet.*;

public class PacketList {
    public static final Class<?>[] packets = {
        EchoPacket.class,
        LoginPacket.class,
        SavePacket.class,
        ScorePacket.class,
        WorldListPacket.class,
        ScoreboardPacket.class,
        MessagePacket.class,
    };

    public static final Class<?>[] extraTypes = {
        byte[].class,
        String[].class,
        int[].class,
        WorldListPacket.WorldListEntry.class,
        WorldListPacket.WorldListEntry[].class,
    };
}
