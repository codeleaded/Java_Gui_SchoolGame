package de.schoolgame.network.packet;

import com.esotericsoftware.kryonet.Connection;
import de.schoolgame.network.Packet;

public class ScorePacket implements Packet {
    public String creator;
    public int score;

    public ScorePacket() {}

    public ScorePacket(String creator, int score) {
        this.creator = creator;
        this.score = score;
    }

    @Override
    public void handle(Connection connection) {

    }
}
