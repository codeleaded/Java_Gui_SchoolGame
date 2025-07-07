package de.schoolgame.server.thread;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import de.schoolgame.network.Packet;
import de.schoolgame.network.PacketIO;
import de.schoolgame.network.packet.EchoPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientThread {
    public Thread thread;
    private final Socket client;

    public ClientThread(Socket client) {
        this.client = client;
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = client.getRemoteAddress();
                Gdx.app.debug("ClientThread " + name, "Connected");

                try (
                    ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(client.getInputStream())
                ) {
                    out.flush(); //Ensure Header is sent

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
        });
    }
}
