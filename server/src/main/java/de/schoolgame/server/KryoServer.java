package de.schoolgame.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.schoolgame.network.PacketList;
import de.schoolgame.network.packet.EchoPacket;
import de.schoolgame.network.packet.LoginPacket;
import de.schoolgame.network.packet.SavePacket;
import de.schoolgame.network.packet.ScorePacket;
import de.schoolgame.server.db.SQLUtils;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class KryoServer {
    Server server;

    public KryoServer() {
        String config = Gdx.files.internal("./config/server.toml").readString();
        TomlParseResult toml = Toml.parse(config);

        server = new Server();

        Kryo kryo = server.getKryo();
        Arrays.stream(PacketList.packets).forEach(kryo::register);
        kryo.register(byte[].class);

        try {
            int tcp = Math.toIntExact(toml.getLong("tcp"));
            int udp = Math.toIntExact(toml.getLong("udp"));
            server.bind(tcp, udp);
        } catch (IOException | NullPointerException e) {
            Gdx.app.error("Server", "Could not bind Server Port", e);
        }

        Listener.TypeListener typeListener = new Listener.TypeListener();

        typeListener.addTypeHandler(EchoPacket.class, (connection, packet) -> {
            Gdx.app.log("Server", "Received EchoPacket: " + packet.message);
            connection.sendTCP(new EchoPacket("Echo: " + packet.message));
        });
        typeListener.addTypeHandler(LoginPacket.class, (connection, packet) -> {
            packet.uuid = UUID.randomUUID().toString();
            SQLUtils.addClient(packet, connection.getRemoteAddressTCP().getAddress().getHostAddress());
            connection.sendTCP(packet);
        });
        typeListener.addTypeHandler(SavePacket.class, (connection, packet) -> {
            if (packet.save == null) {
                packet.save = SQLUtils.getWorld(packet.name);
                connection.sendTCP(packet);
            } else {
                SQLUtils.addWorld(packet);
            }
        });
        typeListener.addTypeHandler(ScorePacket.class, (connection, packet) -> {
            SQLUtils.addScore(packet);
        });

        server.addListener(typeListener);

        server.start();
    }

    public void dispose() {
        server.stop();
    }
}
