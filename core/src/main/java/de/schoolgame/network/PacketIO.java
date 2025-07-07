package de.schoolgame.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketIO {
    public static void writePacket(ObjectOutputStream stream, Packet packet) throws IOException {
        byte id = PacketRegistry.INSTANCE.getPacketID(packet.getClass());
        stream.writeByte(id);
        packet.write(stream);
        stream.flush(); //Ensure Packet is sent
    }

    public static Packet readPacket(ObjectInputStream stream) throws IOException, ReflectiveOperationException {
        byte id = stream.readByte();
        Packet packet = PacketRegistry.INSTANCE.createPacket(id);
        packet.read(stream);
        return packet;
    }
}
