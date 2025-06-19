package de.schoolgame.state;

import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.AssetManager;
import de.schoolgame.render.Camera;
import de.schoolgame.utils.Save;
import de.schoolgame.world.World;
import de.schoolgame.world.WorldObject;
import de.schoolgame.world.entities.PlayerEntity;

public class GameState {
    public static GameState INSTANCE = new GameState();

    public GameStateType state = GameStateType.GAME;

    public DebugState debug;

    public World world;
    public Camera camera;
    public PlayerEntity player;

    public AssetManager assetManager;

    public void loadSave(Save s) {
        WorldObject[][] worldObjects = s.worldObjects();
        world = new World(worldObjects, new Vec2i(worldObjects.length, worldObjects[0].length), s.tileSize(), s.spawn());
        player = new PlayerEntity(world.getSpawn().toVec2f());
        camera  = new Camera();
    }

    public void writeSave() {
        var save = new Save(world.getTiles(), world.getTileSize(), world.getSpawn());
        save.writeSave();
    }

    public enum GameStateType {
        MAIN_MENU,
        GAME,
        DEBUG,
        WORLD_EDITOR,
    }
}
