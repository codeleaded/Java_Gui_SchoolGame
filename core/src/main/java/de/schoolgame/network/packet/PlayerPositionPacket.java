package de.schoolgame.network.packet;

import de.schoolgame.network.Packet;
import de.schoolgame.primitives.Vec2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class PlayerPositionPacket extends Packet {
    private Vec2f position;
    private UUID playerID;

    public PlayerPositionPacket(Vec2f position, UUID playerID) {
        this.position = position;
        this.playerID = playerID;
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeFloat(position.x);
        out.writeFloat(position.y);
        out.writeUTF(playerID.toString());
    }

    @Override
    public void read(ObjectInputStream in) throws IOException {
        position.x = in.readFloat();
        position.y = in.readFloat();
        playerID = UUID.fromString(in.readUTF());
    }
}
