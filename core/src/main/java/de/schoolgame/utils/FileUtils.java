package de.schoolgame.utils;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static int intFromBytes(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
            ((bytes[1] & 0xFF) << 16) |
            ((bytes[2] & 0xFF) << 8 ) |
            (bytes[3] & 0xFF);
    }

    public static byte[] bytesFromInt(int value) {
        return new byte[] {
            (byte)(value >> 24),
            (byte)(value >> 16),
            (byte)(value >> 8),
            (byte)value
        };
    }

    public static File getSaveFile() throws IOException {
        File dir = new File(Gdx.files.getLocalStoragePath() + "/worlds");
        if (!dir.mkdirs() && !dir.exists()) {
            throw new IOException("Could not create directories!");
        }

        File f = new File(dir + "/save.dat");
        if (!f.createNewFile() && !f.exists()) {
            throw new IOException("Could not create File!");
        }

        return f;
    }
}
