package de.schoolgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.schoolgame.network.ServerConnection;
import de.schoolgame.network.packet.EchoPacket;
import de.schoolgame.render.AssetManager;
import de.schoolgame.render.Camera;
import de.schoolgame.state.DebugState;
import de.schoolgame.state.GameState;
import de.schoolgame.world.WorldManager;

import java.util.ArrayList;

public class LoadingTask {
    private boolean finished = false;
    private final ArrayList<Task> tasks;
    private String status;
    private final int initialTasks;

    public LoadingTask() {
        tasks = new ArrayList<>();
        status = "Initializing...";

        addTask("AssetManager", () -> GameState.INSTANCE.assetManager = new AssetManager());
        addTask("WorldManager", () -> GameState.INSTANCE.worldManager = new WorldManager());

        addTask("Camera", () -> GameState.INSTANCE.camera = new Camera());

        FileHandle assets = Gdx.files.internal("assets.txt");

        for (String path : assets.readString().split("\n")) {
            if (path.endsWith(".asset")) {
                addTask("Asset: " + path, () ->
                    GameState.INSTANCE.assetManager.load(path)
                );
            }
            if (path.endsWith(".world")) {
                addTask("World: " + path, () ->
                    GameState.INSTANCE.worldManager.load(Gdx.files.internal(path))
                );
            }
        }

        addTask("DebugState", () -> GameState.INSTANCE.debug = new DebugState());
        addTask("ServerConnection", () -> GameState.INSTANCE.server = new ServerConnection());

        addTask("Networking", () -> {
            GameState.INSTANCE.server.connect();
            GameState.INSTANCE.server.sendPacket(new EchoPacket("Hi!"), true);
        });

        addTask("GUI", () -> GameState.INSTANCE.setState(GameState.GameStateType.CHARACTER_SELECT));

        initialTasks = tasks.size();
    }

    private void addTask(String name, Runnable task) {
        tasks.add(new Task(name, task));
    }

    public void update(long time) {
        long end = System.currentTimeMillis() + time;
        while (end > System.currentTimeMillis() && !finished) {
            update();
        }
    }

    public void update() {
        if (tasks.isEmpty()) {
            finished = true;
            return;
        }
        Task task = tasks.removeFirst();
        status = "Loading " + task.name + "...";
        Gdx.app.log("LoadingTask", status);
        task.task.run();
    }

    public String getStatus() {
        return status;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getInitialTasks() {
        return initialTasks;
    }

    public float getProgress() {
        if(initialTasks == 0) return 1;
        return 1f - (float) tasks.size() / initialTasks;
    }

    private record Task(String name, Runnable task) {}
}
