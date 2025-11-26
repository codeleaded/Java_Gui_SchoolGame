package de.schoolgame.server.state;

import de.schoolgame.server.KryoServer;
import de.schoolgame.server.abstractions.Player;

import java.util.ArrayList;

public class ServerState {
    public static final ServerState INSTANCE = new ServerState();

    public KryoServer server;

    public ArrayList<Player> players = new ArrayList<>();


}
