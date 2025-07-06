package de.schoolgame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.FileUtils;
import de.schoolgame.utils.Save;

import java.io.*;
import java.util.HashMap;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class WorldManager {
    private final HashMap<String, Save> worlds = new HashMap<>();

    public void load(String path) {
        if (path == null) throw new IllegalArgumentException("Path must not be null");

        FileHandle file = Gdx.files.internal(path);
        try (
            InputStream inputStream = file.read();
            InflaterInputStream compressedStream = new InflaterInputStream(inputStream);
            ObjectInputStream ois = new ObjectInputStream(compressedStream)
        ) {
            worlds.put(path, (Save) ois.readObject());
        } catch (IOException | ClassNotFoundException | GdxRuntimeException e) {
            Gdx.app.error("SchoolGame", "Failed to load world \"" + path + "\"", e);
        }
    }

    public Save get(String path) {
        return worlds.get(path + ".world");
    }

    public void save(String filename) {
        var state = GameState.INSTANCE;
        Save save = new Save(state.world.getTiles(), state.world.getTileSize(), state.world.getSpawn());

        try (
            OutputStream outputStream = new FileOutputStream(FileUtils.getSaveFile(filename));
            DeflaterOutputStream compressedStream = new DeflaterOutputStream(outputStream);
            ObjectOutputStream ois = new ObjectOutputStream(compressedStream)
        ) {
            ois.writeObject(save);
        } catch (IOException e) {
            Gdx.app.error("SchoolGame", "Failed to write level!", e);
            throw new RuntimeException(e);
        }
    }
}
