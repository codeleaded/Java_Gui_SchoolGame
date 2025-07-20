package de.schoolgame.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.util.Arrays;

public class ServerConnection {
    private final Client client;
    private String uuid;
    private boolean connected;

    public ServerConnection() {
        connected = false;

        client = new Client();

        Kryo kryo = client.getKryo();
        Arrays.stream(PacketList.packets).forEach(kryo::register);
        kryo.register(byte[].class);
        client.addListener(new ClientListener());

        client.start();
    }

    public boolean connect() {
        String config = Gdx.files.internal("./config/server.toml").readString();
        TomlParseResult toml = Toml.parse(config);

        try {
            String ip = toml.getString("ip");
            int tcp = Math.toIntExact(toml.getLong("tcp"));
            int udp = Math.toIntExact(toml.getLong("udp"));
            client.connect(5000, ip, tcp, udp);
        } catch (IOException | NullPointerException e) {
            Gdx.app.error("ServerConnection", "Could not connect to server!", e);
            return false;
        }

        connected = true;
        return true;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
    public String getUUID() {
        return uuid;
    }

    public void sendPacket(Packet packet, boolean reliable) {
        if (reliable) client.sendTCP(packet);
        else client.sendUDP(packet);
    }

    public void close() {
        client.close();
    }
}
