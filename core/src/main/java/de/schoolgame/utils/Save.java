package de.schoolgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.schoolgame.utils.primitives.Vec2i;
import de.schoolgame.world.Tile;
import de.schoolgame.world.World;

import java.io.*;

public record Save(Tile[][] tiles, int tileSize, Vec2i spawn) implements Serializable {
    public static Save loadSave(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        try (InputStream stream = file.read()) {
            ObjectInputStream ois = new ObjectInputStream(stream);
            Save save = (Save) ois.readObject();
            ois.close();
            return save;
        } catch (IOException | ClassNotFoundException e) {
            Gdx.app.error("SchoolGame", "Failed to load level \"" + fileName + "\"", e);
            World world = new World();
            return new Save(world.getTiles(), world.getTileSize(), world.getSpawn());
        }
    }

    public void writeSave() {
        try (FileOutputStream stream = new FileOutputStream(FileUtils.getSaveFile())) {
            ObjectOutputStream ois = new ObjectOutputStream(stream);
            ois.writeObject(this);
            ois.flush();
            ois.close();
        } catch (IOException e) {
            Gdx.app.error("SchoolGame", "Failed to write level!", e);
            throw new RuntimeException(e);
        }
    }
}
