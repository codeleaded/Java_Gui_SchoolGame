package de.schoolgame.server.db;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.schoolgame.network.packet.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLUtils {
    public static String getStatement(String filename) {
        FileHandle file = Gdx.files.internal("sql/" + filename);
        return file.readString();
    }

    public static void addClient(LoginPacket packet, String ip) {
        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("addClient.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                statement.setString(1, packet.uuid);
                statement.setString(2, ip);
                statement.setString(3, packet.name);
                statement.setInt(4, packet.style);

                statement.execute();
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }
    }

    public static boolean addWorld(SavePacket packet) {
        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("addWorld.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                statement.setString(1, packet.creator);
                statement.setString(2, packet.name);
                statement.setBytes(3, packet.save);

                statement.execute();
                return statement.getUpdateCount() != 0;
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
            return false;
        }
    }

    public static byte[] getWorld(String name) {
        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("getWorld.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                statement.setString(1, name);

                ResultSet res = statement.executeQuery();
                if (res.next()) return res.getBytes("data");
                return null;
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }
        return null;
    }

    public static void addScore(ScorePacket packet) {
        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("addScore.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                statement.setString(1, packet.creator);
                statement.setInt(2, packet.score);

                statement.execute();
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }
    }

    public static WorldListPacket getWorldList() {
        ArrayList<WorldListPacket.WorldListEntry> list = new ArrayList<>();

        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("getWorldList.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                ResultSet res = statement.executeQuery();

                while (res.next()) {
                    String creator = res.getString("creator");
                    String name = res.getString("name");

                    list.add(new WorldListPacket.WorldListEntry(name, creator));
                }
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }

        return new WorldListPacket(list.toArray(new WorldListPacket.WorldListEntry[0]));
    }

    public static ScoreboardPacket getTopScores() {
        String[] names = new String[10];
        int[] scores = new int[10];

        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("getTopScores.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                ResultSet res = statement.executeQuery();

                for (int i = 0; i < 10; i++) {
                    if (res.next()) {
                        scores[i] = res.getInt("score");
                        names[i] = res.getString("name");
                    } else {
                        scores[i] = 0;
                        names[i] = "";
                    }
                }
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }

        return new ScoreboardPacket(names, scores);
    }
}
