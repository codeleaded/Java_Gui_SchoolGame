package de.schoolgame.server.thread;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream())
        ) {
            String input;
            while ((input = in.readLine()) != null) {
                Gdx.app.debug("ClientThread " + name, "Got input: \"" + input + "\"");

                String response = "Echo: " + input;
                out.write(response + "\n");
                Gdx.app.debug("ClientThread " + name, "Wrote: \"" + response + "\"");

                out.flush();
            }
        } catch (IOException e) {
            Gdx.app.error("ClientThread " + name, "Error: " + e);
        } finally {
            client.dispose();
        }

        Gdx.app.debug("ClientThread " + name, "Disconnected");
    }
}
