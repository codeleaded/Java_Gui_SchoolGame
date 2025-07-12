package de.schoolgame.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.schoolgame.network.PacketList;
import de.schoolgame.network.packet.EchoPacket;
import de.schoolgame.network.packet.LoginPacket;
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

        try {
            String ip = toml.getString("ip");
            int tcp = Math.toIntExact(toml.getLong("tcp"));
            int udp = Math.toIntExact(toml.getLong("udp"));
            server.bind(5500, 5501);
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
            connection.sendTCP(packet);
        });
        server.addListener(typeListener);

        server.start();
    }

    public void dispose() {
        server.stop();
    }
}
