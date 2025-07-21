package de.schoolgame.server.db;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.schoolgame.network.packet.LoginPacket;
import de.schoolgame.network.packet.SavePacket;
import de.schoolgame.network.packet.ScorePacket;
import de.schoolgame.network.packet.WorldListPacket;

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
                    int id = res.getInt("id");
                    String creator = res.getString("creator");
                    String name = res.getString("name");

                    list.add(new WorldListPacket.WorldListEntry(id, name, creator));
                }
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }

        WorldListPacket packet = new WorldListPacket(list.toArray(new WorldListPacket.WorldListEntry[0]));
        return packet;
    }

    public static ScorePacket[] getTopScores() {
        ScorePacket[] scores = new ScorePacket[10];

        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("getTopScores.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                ResultSet res = statement.executeQuery();

                for (int i = 0; i < 10; i++) {
                    ScorePacket score = new ScorePacket();
                    score.score = res.getInt("score");
                    score.creator = res.getString("name");
                    scores[i] = score;
                }
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }

        return scores;
    }
}
