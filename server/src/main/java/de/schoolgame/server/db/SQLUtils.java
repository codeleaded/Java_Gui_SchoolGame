package de.schoolgame.server.db;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SQLUtils {
    public static String getStatement(String filename) {
        FileHandle file = Gdx.files.internal("./sql/" + filename);
        return file.readString();
    }
}
