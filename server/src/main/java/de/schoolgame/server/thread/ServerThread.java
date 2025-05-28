package de.schoolgame.server.thread;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;

public class ServerThread extends Thread {
    @Override
    public void run() {
        ServerSocketHints hints = new ServerSocketHints();
        hints.acceptTimeout = 0;

        ServerSocket socket = Gdx.net.newServerSocket(Net.Protocol.TCP, 5500, hints);

        while (true) {
            new ClientThread(socket.accept(null)).start();
        }
    }
}
