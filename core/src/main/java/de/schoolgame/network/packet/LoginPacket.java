package de.schoolgame.network.packet;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import de.schoolgame.network.Packet;
import de.schoolgame.state.GameState;

public class LoginPacket implements Packet {
    public String name;
    public String uuid;
    public int style;

    public LoginPacket() {}

    public LoginPacket(String name, String uuid, int style) {
        this.name = name;
        this.uuid = uuid;
        this.style = style;
    }

    @Override
    public void handle(Connection connection) {
        var state = GameState.INSTANCE;

        state.username = name;
        state.server.setUUID(uuid);
        state.playerStyle = style;

        Gdx.app.log("LoginPacket", "Login! " + name + " (" + uuid + ")" + style);
    }
}
