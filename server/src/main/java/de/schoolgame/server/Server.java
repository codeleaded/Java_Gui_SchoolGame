package de.schoolgame.server;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import de.schoolgame.server.db.Database;
import de.schoolgame.server.state.ServerState;

public class Server extends ApplicationAdapter {
    @Override
    public void create() {
        Gdx.app.log("Server", "Server starting...");
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Database.init();
        ServerState.INSTANCE.server = new KryoServer();

        Gdx.app.log("Server", "Server started");
    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {
        ServerState.INSTANCE.server.dispose();
        Gdx.app.log("Server", "Server stopped");
    }
}
