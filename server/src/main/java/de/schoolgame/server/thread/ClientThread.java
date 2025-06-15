package de.schoolgame.server.thread;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import de.schoolgame.network.Packet;
import de.schoolgame.network.PacketIO;
import de.schoolgame.network.packet.EchoPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientThread extends Thread {
    private final Socket client;

    public ClientThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        String name = client.getRemoteAddress();
        Gdx.app.debug("ClientThread " + name, "Connected");

        try (
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream())
        ) {
            Packet p = PacketIO.readPacket(in);
            if (p instanceof EchoPacket echoPacket) {
                echoPacket.setMessage("Echo: " + echoPacket.getMessage());
                PacketIO.writePacket(out, echoPacket);
            }
        } catch (IOException | ReflectiveOperationException e) {
            Gdx.app.error("ClientThread " + name, "Error: " + e);
        } finally {
            client.dispose();
            Gdx.app.debug("ClientThread " + name, "Disconnected");
        }
    }
}
