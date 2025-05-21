package de.schoolgame.state;

import de.schoolgame.render.Camera;
import de.schoolgame.utils.Save;
import de.schoolgame.world.World;
import de.schoolgame.world.entities.Player;

public class GameState {
    public static GameState INSTANCE = new GameState();

    public DebugState debug = new DebugState();

    public World world = new World();
    public Camera camera = new Camera();
    public Player player = new Player();

    public void loadSave(Save s) {
        var tiles = s.tiles();
        this.world = new World(tiles, tiles.length, tiles[0].length, s.tileSize());
    }

    public void writeSave() {
        var save = new Save(world.getTiles(), world.getTileSize());
        save.writeSave();
    }
}
