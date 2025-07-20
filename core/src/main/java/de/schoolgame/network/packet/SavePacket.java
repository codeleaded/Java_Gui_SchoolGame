package de.schoolgame.network.packet;

import com.esotericsoftware.kryonet.Connection;
import de.schoolgame.network.Packet;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.Save;

public class SavePacket implements Packet {
    public int id;
    public String creator;
    public String name;
    public byte[] save;

    public SavePacket() {}

    public SavePacket(int id, String creator, String name, byte[] save) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.save = save;
    }

    @Override
    public void handle(Connection connection) {
        var state = GameState.INSTANCE;
        state.worldManager.load(this.name, this.save);
        Save s = state.worldManager.get(this.name);
        state.loadSave(s);
    }
}
