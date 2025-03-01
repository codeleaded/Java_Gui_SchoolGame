package de.schoolgame.state;

public class GameState {
    public static GameState INSTANCE = new GameState();

    public boolean debug = false;
    public float[] bg_color = {0.15f, 0.15f, 0.2f};
    public float[] animation_delay = {0.025f};
}
