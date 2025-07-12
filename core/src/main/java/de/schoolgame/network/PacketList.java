package de.schoolgame.network;

import de.schoolgame.network.packet.EchoPacket;
import de.schoolgame.network.packet.LoginPacket;

public class PacketList {
    public static final Class<?>[] packets = {
        EchoPacket.class,
        LoginPacket.class
    };
}
