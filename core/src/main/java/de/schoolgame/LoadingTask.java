package de.schoolgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import de.schoolgame.render.AssetManager;
import de.schoolgame.state.DebugState;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.Save;

import java.util.ArrayList;
import java.util.Arrays;

public class LoadingTask {
    private boolean finished = false;
    private final ArrayList<Task> tasks;
    private String status;
    private final int initialTasks;

    public LoadingTask() {
        tasks = new ArrayList<>();
        status = "Initializing...";

        addTask("AssetManager", () -> GameState.INSTANCE.assetManager = new AssetManager());

        ArrayList<FileHandle> paths = new ArrayList<>();
        paths.add(Gdx.files.internal("."));

        while (!paths.isEmpty()) {
            FileHandle file = paths.removeFirst();
            if (file.isDirectory()) {
                paths.addAll(Arrays.asList(file.list()));
            } else {
                String path = file.path();
                if (path.endsWith(".png")) {
                    addTask("Assets", () ->
                        GameState.INSTANCE.assetManager.load(path, Texture.class)
                    );
                }
            }
        }

        addTask("DebugState", () -> GameState.INSTANCE.debug = new DebugState());

        addTask("Networking", () -> {
            //TODO Networking
            //networkTest = new NetworkTest();
            //networkTest.test();
        });

        addTask("World", () -> GameState.INSTANCE.loadSave(Save.loadSave("worlds/save.dat")));

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
        Gdx.app.log("INFO", status);
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
