package dev.arteshow.essentials.storage;

import java.sql.Connection;
import java.sql.SQLException;

public interface Migration {
    int getVersion();
    String getName();

    void execute(Connection connection) throws SQLException;
}