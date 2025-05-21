package de.schoolgame.state;

import de.schoolgame.render.Camera;
import de.schoolgame.world.World;
import de.schoolgame.world.entities.Player;

public class GameState {
    public static GameState INSTANCE = new GameState();

    public DebugState debug = new DebugState();

    public World world = null;
    public Camera camera = new Camera();
    public Player player = new Player();

}
