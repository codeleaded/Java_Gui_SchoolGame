package de.schoolgame.network;

import de.schoolgame.network.packet.EchoPacket;

import java.util.ArrayList;

public class PacketRegistry {
    public static final PacketRegistry INSTANCE = new PacketRegistry();

    private final ArrayList<Class<? extends Packet>> packets = new ArrayList<>();

    public PacketRegistry() {
        registerPacket(EchoPacket.class);
    }

    public void registerPacket(Class<? extends Packet> clazz) {
        packets.add(clazz);
    }

    public Packet createPacket(byte id) throws ReflectiveOperationException {
        Class<? extends Packet> clazz = getPacketClass(id);
        if (clazz == null) throw new IllegalArgumentException("No such packet id: " + id);
        return clazz.getDeclaredConstructor().newInstance();
    }

    public Class<? extends Packet> getPacketClass(byte id) {
        return packets.get(id);
    }

    public byte getPacketID(Class<? extends Packet> clazz) {
        return (byte) packets.indexOf(clazz);
    }
}
