package de.schoolgame.network.packet;

import de.schoolgame.network.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EchoPacket extends Packet {
    private String message;

    public EchoPacket() {}

    public EchoPacket(String message) {
        this.message = message;
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeUTF(message);
    }

    @Override
    public void read(ObjectInputStream in) throws IOException {
        message = in.readUTF();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
