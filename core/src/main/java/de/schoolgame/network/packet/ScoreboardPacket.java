package de.schoolgame.network.packet;

import com.esotericsoftware.kryonet.Connection;
import de.schoolgame.network.Packet;
import de.schoolgame.render.gui.screens.ScoreboardScreen;
import de.schoolgame.state.GameState;

public class ScoreboardPacket implements Packet {
    public String[] names;
    public int[] scores;

    public ScoreboardPacket() {}
    public ScoreboardPacket(String[] names, int[] scores) {
        this.names = names;
        this.scores = scores;
    }

    @Override
    public void handle(Connection connection) {
        var state = GameState.INSTANCE;

        if (state.screen instanceof ScoreboardScreen s) {
            s.setData(this.names, this.scores);
        }
    }
}
