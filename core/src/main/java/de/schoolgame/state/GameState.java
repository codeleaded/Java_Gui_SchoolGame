package de.schoolgame.state;

import de.schoolgame.network.ServerConnection;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.render.AssetManager;
import de.schoolgame.render.Camera;
import de.schoolgame.render.gui.Screen;
import de.schoolgame.render.gui.screens.*;
import de.schoolgame.utils.Save;
import de.schoolgame.world.World;
import de.schoolgame.world.WorldManager;
import de.schoolgame.world.entities.PlayerEntity;

public class GameState {
    public static GameState INSTANCE = new GameState();

    private GameStateType state = GameStateType.MAIN_MENU;

    public Screen screen;

    public String username = "Anonym";
    public int playerStyle = 1;

    public DebugState debug;
    public ServerConnection server;

    public World world;
    public Camera camera;
    public PlayerEntity player;

    public int score = 0;

    public AssetManager assetManager;
    public WorldManager worldManager;

    public boolean escapeFlag = false;

    public void loadSave(Save s) {
        world = new World(s);
        player = new PlayerEntity(world.getSpawn().toVec2f().add(new Vec2f(0.0f,0.001f)));
    }

    public boolean controllable() {
        return state == GameStateType.GAME || state == GameStateType.DEBUG || state == GameStateType.WORLD_EDITOR;
    }

    public void setState(GameStateType state) {
        this.state = state;
        switch (state) {
            case CHARACTER_SELECT:
                this.screen = new CharacterSelectScreen();
                break;
            case MAIN_MENU:
                this.screen = new MainMenuScreen();
                break;
            case WORLD_SELECT:
                this.screen = new WorldSelectScreen();
                break;
            case SCOREBOARD:
                this.screen = new ScoreboardScreen();
                break;
            case WORLD_EDITOR:
            case DEBUG:
            case GAME:
                this.screen = new HudScreen();
                break;
            case CREDITS:
                this.screen = new CreditScreen();
                break;
        }
    }

    public GameStateType getState() {
        return state;
    }

    public enum GameStateType {
        CHARACTER_SELECT,
        SCOREBOARD,
        MAIN_MENU,
        WORLD_SELECT,
        WORLD_EDITOR,
        GAME,
        DEBUG,
        CREDITS,
    }
}
