package de.schoolgame.server;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import de.schoolgame.server.thread.ServerThread;

public class Server extends ApplicationAdapter {
    ServerThread serverThread;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        serverThread = new ServerThread();
        serverThread.start();
    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {
        serverThread.interrupt();
    }
}
