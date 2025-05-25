package de.schoolgame.state;

import de.schoolgame.render.Camera;
import de.schoolgame.utils.Save;
import de.schoolgame.utils.primitives.Vec2i;
import de.schoolgame.world.World;
import de.schoolgame.world.entities.Player;

public class GameState {
    public static GameState INSTANCE = new GameState();

    public DebugState debug = new DebugState();

    public World world = null;
    public Camera camera = null;
    public Player player = null;

    public void loadSave(Save s) {
        var tiles = s.tiles();
        world = new World(tiles, new Vec2i(tiles.length, tiles[0].length), s.tileSize(), s.spawn());
        player = new Player(world.getSpawn().toVec2f());
        camera  = new Camera();
    }

    public void writeSave() {
        var save = new Save(world.getTiles(), world.getTileSize(), world.getSpawn());
        save.writeSave();
    }
}
