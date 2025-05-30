package de.schoolgame.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketIO {
    public static void writePacket(DataOutputStream stream, Packet packet) throws IOException {
        byte id = PacketRegistry.INSTANCE.getPacketID(packet.getClass());
        stream.writeByte(id);
        packet.write(stream);
    }

    public static Packet readPacket(DataInputStream stream) throws IOException, ReflectiveOperationException {
        byte id = stream.readByte();
        Packet packet = PacketRegistry.INSTANCE.createPacket(id);
        packet.read(stream);
        return packet;
    }
}
