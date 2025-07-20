package de.schoolgame.network;

import de.schoolgame.network.packet.EchoPacket;
import de.schoolgame.network.packet.LoginPacket;
import de.schoolgame.network.packet.SavePacket;

public class PacketList {
    public static final Class<?>[] packets = {
        EchoPacket.class,
        LoginPacket.class,
        SavePacket.class,
    };
}
