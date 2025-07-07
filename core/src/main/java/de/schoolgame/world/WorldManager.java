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
    public static String WORLD_FOLDER = "./worlds/";

    private final HashMap<String, Save> worlds = new HashMap<>();

    public void load(String worldName) {
        FileHandle file = Gdx.files.internal(WORLD_FOLDER + worldName + ".world");
        load(file);
    }

    public void load(FileHandle file) {
        if (file == null) throw new IllegalArgumentException("File must not be null");

        try (
            InputStream inputStream = file.read();
            InflaterInputStream compressedStream = new InflaterInputStream(inputStream);
            ObjectInputStream ois = new ObjectInputStream(compressedStream)
        ) {
            worlds.put(file.nameWithoutExtension(), (Save) ois.readObject());
        } catch (IOException | ClassNotFoundException | GdxRuntimeException e) {
            Gdx.app.error("SchoolGame", "Failed to load world \"" + file.path() + "\"", e);
        }
    }

    public Save get(String worldName) {
        return worlds.get(worldName);
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
