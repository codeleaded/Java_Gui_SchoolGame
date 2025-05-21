package de.schoolgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.schoolgame.world.Tile;

import java.io.*;

public record Save(Tile[][] tiles, int tileSize) implements Serializable {
    public static Save loadSave(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        try (InputStream stream = file.read()) {
            ObjectInputStream ois = new ObjectInputStream(stream);
            Save save = (Save) ois.readObject();
            ois.close();
            return save;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void writeSave() {
        try (FileOutputStream stream = new FileOutputStream(FileUtils.getSaveFile())) {
            ObjectOutputStream ois = new ObjectOutputStream(stream);
            ois.writeObject(this);
            ois.flush();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
