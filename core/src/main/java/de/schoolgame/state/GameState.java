package de.schoolgame.state;

import de.schoolgame.render.AssetManager;
import de.schoolgame.render.Camera;
import de.schoolgame.utils.Save;
import de.schoolgame.world.World;
import de.schoolgame.world.WorldManager;
import de.schoolgame.world.entities.PlayerEntity;

public class GameState {
    public static GameState INSTANCE = new GameState();

    public GameStateType state = GameStateType.MAIN_MENU;

    public DebugState debug;

    public World world;
    public Camera camera;
    public PlayerEntity player;

    public AssetManager assetManager;
    public WorldManager worldManager;

    public boolean escapeFlag = false;

    public void loadSave(Save s) {
        world = new World(s);
        player = new PlayerEntity(world.getSpawn().toVec2f());
    }

    public boolean controllable() {
        return state == GameStateType.GAME || state == GameStateType.DEBUG;
    }

    public enum GameStateType {
        MAIN_MENU,
        GAME,
        DEBUG,
    }
}
