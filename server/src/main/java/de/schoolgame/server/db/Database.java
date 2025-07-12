package de.schoolgame.server.db;

import com.badlogic.gdx.Gdx;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private static final HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/schoolgame");
        config.setUsername("root");
        config.setPassword(System.getenv("POSTGRES_PASSWORD"));

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(2000); // Optional: detect leaked connections

        ds = new HikariDataSource(config);
    }

    public static void init() {
        try (Connection connection = Database.getConnection()) {
            String s = SQLUtils.getStatement("migrate.sql");
            try (PreparedStatement statement = connection.prepareStatement(s)) {
                Gdx.app.log("Database", "Database initializing...");
                statement.execute();
                Gdx.app.log("Database", "Database initialized");
            }
        } catch (SQLException e) {
            Gdx.app.error("Database", "SQL Error: " + e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void close() {
        ds.close();
    }
}
