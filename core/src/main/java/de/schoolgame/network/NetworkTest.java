package de.schoolgame.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import de.schoolgame.network.packet.EchoPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NetworkTest {
    public NetworkTest() {
        SocketHints hints = new SocketHints();

        Socket socket;
        try {
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 5500, hints);
        } catch (Exception e) {
            Gdx.app.error("NetworkTest", "Error connecting to server", e);
            return;
        }

        Gdx.app.log("NetworkTest", "Connected to server");

        try (
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream())
        ) {
            PacketIO.writePacket(out, new EchoPacket("Hello World!"));

            Packet p = PacketIO.readPacket(in);
            if (p instanceof EchoPacket echoPacket) {
                Gdx.app.log("NetworkTest", "Echo packet received: " + echoPacket.getMessage());
            }
        } catch (IOException | ReflectiveOperationException e) {
            Gdx.app.error("NetworkTest", "Error communicating to server", e);
        }

        socket.dispose();

        Gdx.app.log("NetworkTest", "Disconnected from server");
    }
}
