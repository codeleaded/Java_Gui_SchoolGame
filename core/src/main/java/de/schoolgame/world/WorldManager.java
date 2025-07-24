package de.schoolgame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.schoolgame.network.packet.SavePacket;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.FileUtils;
import de.schoolgame.utils.Save;

import java.io.*;
import java.util.HashMap;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class WorldManager {
    public static String WORLD_FOLDER = "assets/worlds/";

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
            Gdx.app.error("SchoolGame", "Failed to load World \"" + file.path() + "\"", e);
        }
    }

    public void load(String name, byte[] data) {
        if (data == null) throw new IllegalArgumentException("Data must not be null");

        try (
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            InflaterInputStream compressedStream = new InflaterInputStream(inputStream);
            ObjectInputStream ois = new ObjectInputStream(compressedStream)
        ) {
            worlds.put(name, (Save) ois.readObject());
        } catch (IOException | ClassNotFoundException | GdxRuntimeException e) {
            Gdx.app.error("SchoolGame", "Failed to load Server-World \"" + name + "\"", e);
        }
    }

    public Save get(String worldName) {
        GameState.INSTANCE.worldName = worldName;
        return worlds.get(worldName);
    }

    public void save(String filename) {
        var state = GameState.INSTANCE;
        Save save = new Save(state.world.getTiles(), state.world.getTileSize(), state.world.getSpawn());

        try (
            OutputStream outputStream = new FileOutputStream(FileUtils.getSaveFile(filename));
            DeflaterOutputStream compressedStream = new DeflaterOutputStream(outputStream);
            ObjectOutputStream oos = new ObjectOutputStream(compressedStream)
        ) {
            oos.writeObject(save);
        } catch (IOException e) {
            Gdx.app.error("SchoolGame", "Failed to write level!", e);
            throw new RuntimeException(e);
        }
    }

    public void upload(String name) {
        var state = GameState.INSTANCE;
        if (!state.server.isConnected()) return;

        Save save = new Save(state.world.getTiles(), state.world.getTileSize(), state.world.getSpawn());

        try (
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DeflaterOutputStream dos = new DeflaterOutputStream(baos);
            ObjectOutputStream oos = new ObjectOutputStream(dos)
        ) {
            oos.writeObject(save);
            oos.flush();
            dos.finish();

            byte[] data = baos.toByteArray();

            SavePacket packet = new SavePacket(-1, state.server.getUUID(), name, data);
            state.server.sendPacket(packet, true);

        } catch (IOException e) {
            Gdx.app.error("SchoolGame", "Failed to upload level!", e);
        }
    }

    public void download(String name) {
        var state = GameState.INSTANCE;
        if (!state.server.isConnected()) return;

        SavePacket packet = new SavePacket(-1, "", name, null);

        state.server.sendPacket(packet, true);
    }
}
