package de.schoolgame.state;

import de.schoolgame.world.World;

public class GameState {
    public static GameState INSTANCE = new GameState();

    public boolean debug = false;
    public float[] bg_color = {0.15f, 0.15f, 0.2f};
    public float[] animation_delay = {0.025f};

    public World world = new World("worlds/test.dat");

    public boolean leftMove = false;
    public boolean rightMove = false;
}
