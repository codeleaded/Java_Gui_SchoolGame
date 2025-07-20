package de.schoolgame.server.db;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.schoolgame.network.packet.LoginPacket;
import de.schoolgame.network.packet.SavePacket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils {
    public static String getStatement(String filename) {
        FileHandle file = Gdx.files.internal("./sql/" + filename);
        return file.readString();
    }

    public static void addClient(LoginPacket packet, String ip) {
        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("addClient.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                statement.setString(1, packet.uuid);
                statement.setString(2, ip);
                statement.setString(3, packet.name);

                statement.execute();
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }
    }

    public static void addWorld(SavePacket packet) {
        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("addWorld.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                statement.setString(1, packet.creator);
                statement.setString(2, packet.name);
                statement.setBytes(3, packet.save);

                statement.execute();
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }
    }

    public static byte[] getWorld(String name) {
        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("getWorld.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                statement.setString(1, name);

                ResultSet res = statement.executeQuery();
                res.next();
                return res.getBytes("data");
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }
        return null;
    }
}
