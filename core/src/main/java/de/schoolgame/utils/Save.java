package de.schoolgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.world.World;
import de.schoolgame.world.WorldObject;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public record Save(WorldObject[][] worldObjects, int tileSize, Vec2i spawn) implements Serializable {
    public static Save loadSave(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        try (
            InputStream inputStream = file.read();
            InflaterInputStream compressedStream = new InflaterInputStream(inputStream);
            ObjectInputStream ois = new ObjectInputStream(compressedStream)
        ) {
            return (Save) ois.readObject();
        } catch (IOException | ClassNotFoundException | GdxRuntimeException e) {
            Gdx.app.error("SchoolGame", "Failed to load level \"" + fileName + "\"", e);
            World world = new World();
            return new Save(world.getTiles(), world.getTileSize(), world.getSpawn());
        }
    }

    public void writeSave() {
        try (
            OutputStream outputStream = new FileOutputStream(FileUtils.getSaveFile());
            DeflaterOutputStream compressedStream = new DeflaterOutputStream(outputStream);
            ObjectOutputStream ois = new ObjectOutputStream(compressedStream)
        ) {
            ois.writeObject(this);
        } catch (IOException e) {
            Gdx.app.error("SchoolGame", "Failed to write level!", e);
            throw new RuntimeException(e);
        }
    }
}
