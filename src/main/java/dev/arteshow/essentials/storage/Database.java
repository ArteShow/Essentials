package dev.arteshow.essentials.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {

    private Connection connection;
    private final File databaseFile;

    public Database(File dataFolder) {
        this.databaseFile = new File(dataFolder, "essentials.db");
    }

    public void open() throws SQLException {
        if (!connection.isClosed()) {
            return;
        }

        if (!databaseFile.getParentFile().exists()) {
            databaseFile.getParentFile().mkdirs();
        }

        this.connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
