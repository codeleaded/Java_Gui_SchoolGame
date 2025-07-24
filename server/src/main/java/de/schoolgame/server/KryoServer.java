package de.schoolgame.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.schoolgame.network.PacketList;
import de.schoolgame.network.packet.*;
import de.schoolgame.server.db.SQLUtils;
import de.schoolgame.utils.AssetUtils;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class KryoServer {
    Server server;

    public KryoServer() {
        TomlParseResult toml = AssetUtils.getAsset("config/server.toml");
        int tcp = AssetUtils.getTcpPort(toml);
        int udp = AssetUtils.getUdpPort(toml);

        server = new Server();

        Kryo kryo = server.getKryo();
        Arrays.stream(PacketList.packets).forEach(kryo::register);
        Arrays.stream(PacketList.extraTypes).forEach(kryo::register);

        try {
            server.bind(tcp, udp);
        } catch (IOException | NullPointerException e) {
            Gdx.app.error("Server", "Could not bind Server Port", e);
        }

        Listener.TypeListener typeListener = new Listener.TypeListener();

        typeListener.addTypeHandler(EchoPacket.class, (connection, packet) -> {
            Gdx.app.log("EchoPacket", getLogHeader(connection) + ": " + packet.message);
            connection.sendTCP(new EchoPacket("Echo: " + packet.message));
        });
        typeListener.addTypeHandler(LoginPacket.class, (connection, packet) -> {
            packet.uuid = UUID.randomUUID().toString();
            Gdx.app.log("LoginPacket",  getLogHeader(connection) + ": " + packet.name + " (" + packet.style + ") " + packet.uuid);
            SQLUtils.addClient(packet, getIP(connection));
            connection.sendTCP(packet);
        });
        typeListener.addTypeHandler(SavePacket.class, (connection, packet) -> {
            if (packet.save == null) {
                packet.save = SQLUtils.getWorld(packet.name);
                if (packet.save == null) {
                    Gdx.app.log("SavePacket", getLogHeader(connection) + ": " + packet.name + " existiert nicht!");
                    connection.sendTCP(new MessagePacket(packet.name + " existiert nicht!"));
                } else {
                    Gdx.app.log("SavePacket", getLogHeader(connection) + " hat Welt " + packet.name + " angefordert");
                    connection.sendTCP(packet);
                }
            } else {
                if (!SQLUtils.addWorld(packet)) {
                    connection.sendTCP(new MessagePacket("Welt mit dem Namen \"" + packet.name + "\" existiert schon!"));
                    Gdx.app.log("SavePacket", getLogHeader(connection) + ": " + packet.name + " existiert schon!");
                } else {
                    Gdx.app.log("SavePacket", getLogHeader(connection) + " hat " + packet.name + " hochgeladen");
                }
            }
        });

        typeListener.addTypeHandler(ScorePacket.class, (connection, packet) -> SQLUtils.addScore(packet));
        typeListener.addTypeHandler(ScoreboardPacket.class,
            (connection, packet) -> connection.sendTCP(SQLUtils.getTopScores()));
        typeListener.addTypeHandler(WorldListPacket.class,
            (connection, packet) -> connection.sendTCP(SQLUtils.getWorldList()));

        server.addListener(typeListener);

        server.start();
    }

    private String getLogHeader(Connection connection) {
        return "[(" + connection.getID() + ") " + getIP(connection) + "]";
    }

    private String getIP(Connection connection) {
        return connection.getRemoteAddressTCP().getAddress().getHostAddress();
    }

    public void dispose() {
        server.stop();
    }
}
