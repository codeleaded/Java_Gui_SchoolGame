package de.schoolgame.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import de.schoolgame.network.packet.EchoPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@SuppressWarnings("unused")
public class NetworkTest {
    private boolean success = false;

    public void test() {
        success = false;
        SocketHints hints = new SocketHints();

        Socket socket;
        try {
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, "10.0.3.18", 5500, hints);
        } catch (Exception e) {
            Gdx.app.error("NetworkTest", "Error connecting to server", e);
            return;
        }

        Gdx.app.log("NetworkTest", "Connected to server");

        try (
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            out.flush(); //Ensure Header is sent

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
        success = true;
    }

    public boolean hasSucceeded() {
        return success;
    }
}
