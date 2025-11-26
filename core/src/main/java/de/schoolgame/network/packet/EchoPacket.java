package de.schoolgame.network.packet;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import de.schoolgame.network.Packet;

public class EchoPacket implements Packet {
    public String message;

    public EchoPacket() {}

    public EchoPacket(String message) {
        this.message = message;
    }

    @Override
    public void handle(Connection connection) {
        Gdx.app.log("EchoPacket", message);
    }
}
