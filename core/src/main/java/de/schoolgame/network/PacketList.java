package de.schoolgame.network;

import de.schoolgame.network.packet.*;

public class PacketList {
    public static final Class<?>[] packets = {
        EchoPacket.class,
        LoginPacket.class,
        SavePacket.class,
        ScorePacket.class,
        WorldListPacket.class,
    };
}
