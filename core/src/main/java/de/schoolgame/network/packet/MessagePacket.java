package de.schoolgame.network.packet;

import com.esotericsoftware.kryonet.Connection;
import de.schoolgame.network.Packet;
import de.schoolgame.state.GameState;

public class MessagePacket implements Packet {
    public String message;

    public MessagePacket() {}
    public MessagePacket(String message) {
        this.message = message;
    }

    @Override
    public void handle(Connection connection) {
        var state = GameState.INSTANCE;
        state.message = message;
        state.messageRemaining = 2;
    }
}
