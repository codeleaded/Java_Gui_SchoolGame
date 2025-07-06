package de.schoolgame.utils;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static File getSaveFile(String filename) throws IOException {
        File dir = new File(Gdx.files.getLocalStoragePath() + "/worlds");
        if (!dir.mkdirs() && !dir.exists()) {
            throw new IOException("Could not create directories!");
        }

        File f = new File(dir + "/" + filename + ".world");
        if (!f.createNewFile() && !f.exists()) {
            throw new IOException("Could not create File!");
        }

        return f;
    }
}
