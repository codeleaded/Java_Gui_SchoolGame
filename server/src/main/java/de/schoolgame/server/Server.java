package de.schoolgame.server;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import de.schoolgame.server.thread.ServerThread;

public class Server extends ApplicationAdapter {
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        new ServerThread().start();
    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
