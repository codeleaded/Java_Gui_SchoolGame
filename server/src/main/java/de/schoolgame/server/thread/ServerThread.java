package de.schoolgame.server.thread;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ServerThread extends Thread {
    private final ArrayList<ClientThread> clientThreads;
    private final ServerSocket serverSocket;

    public ServerThread() {
        clientThreads = new ArrayList<>();
        ServerSocketHints hints = new ServerSocketHints();

        hints.acceptTimeout = 5000;

        hints.performancePrefBandwidth = 0;
        hints.performancePrefLatency = 2;
        hints.performancePrefConnectionTime = 1;

        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 5500, hints);
    }

    @Override
    public void run() {


        while (!isInterrupted()) {
            try {
                Socket s = serverSocket.accept(null); // wait for new socket
                handleConnection(s);
            } catch (GdxRuntimeException e) {
                // Timeout reached
            }
        }

        dispose();
    }

    private void handleConnection(Socket s) {
        ClientThread clientThread = new ClientThread(s);
        clientThread.thread.start();
        clientThreads.add(clientThread);
    }

    public void dispose() {
        for (ClientThread clientThread : clientThreads) {
            clientThread.thread.interrupt();
        }

        serverSocket.dispose();
    }
}
