package de.schoolgame.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String line = "Hello, server!";
            writer.write(line + "\n");
            writer.flush();
            Gdx.app.log("NetworkTest", "Wrote to server: \"" + line + "\"");

            String response = reader.readLine();
            Gdx.app.log("NetworkTest", "Received response: \"" + response + "\"");
        } catch (IOException e) {
            Gdx.app.error("NetworkTest", "Error communicating to server", e);
        }

        socket.dispose();

        Gdx.app.log("NetworkTest", "Disconnected from server");
    }
}
