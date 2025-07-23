package de.schoolgame.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import de.schoolgame.utils.AssetUtils;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.util.Arrays;

public class ServerConnection {
    private final Client client;
    private String uuid;

    public boolean initiallyConnected = false;
    private boolean connected;

    public ServerConnection() {
        connected = false;

        client = new Client();

        Kryo kryo = client.getKryo();
        Arrays.stream(PacketList.packets).forEach(kryo::register);
        Arrays.stream(PacketList.extraTypes).forEach(kryo::register);
        client.addListener(new ClientListener());

        client.start();
    }

    public void connect() {
        TomlParseResult toml = AssetUtils.getAsset("config/server.toml");
        String ip = AssetUtils.getServerIP(toml);
        int tcp = AssetUtils.getTcpPort(toml);
        int udp = AssetUtils.getUdpPort(toml);

        try {
            client.connect(5000, ip, tcp, udp);
        } catch (IOException | NullPointerException e) {
            Gdx.app.error("ServerConnection", "Could not connect to server!", e);
            return;
        }

        connected = true;
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
        if (!initiallyConnected) return;

        if (!client.isConnected()) {
            connect();
            if (!connected) {
                return;
            }
        }

        if (reliable) client.sendTCP(packet);
        else client.sendUDP(packet);
    }

    public void close() {
        client.close();
    }
}
