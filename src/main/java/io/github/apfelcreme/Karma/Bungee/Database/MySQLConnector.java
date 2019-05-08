package io.github.apfelcreme.Karma.Bungee.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
 * <p>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme
 */
public class MySQLConnector {

    private static MySQLConnector instance = null;

    /**
     * a hikariCp data source
     */
    private HikariDataSource dataSource;

    /**
     * returns the MySQLConnector instance
     *
     * @return the connector instance
     */
    public static MySQLConnector getInstance() {
        if (instance == null) {
            instance = new MySQLConnector();
        }
        return instance;
    }

    /**
     * initializes the database connection
     */
    public void initConnection() {
        if (KarmaPluginConfig.getInstance().getSqlDatabase() != null && !KarmaPluginConfig.getInstance().getSqlDatabase().isEmpty()) {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://" + KarmaPluginConfig.getInstance().getSqlUrl()
                    + "/" + KarmaPluginConfig.getInstance().getSqlDatabase());
            hikariConfig.setUsername(KarmaPluginConfig.getInstance().getSqlUser());
            hikariConfig.setPassword(KarmaPluginConfig.getInstance().getSqlPassword());

            dataSource = new HikariDataSource(hikariConfig);
            initTables();
        }
    }

    /**
     * returns a connection from a HikariCP connection pool
     *
     * @return a connection from a HikariCP connection pool
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * creates the tables if they dont exist yet
     */
    public void initTables() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE DATABASE IF NOT EXISTS " + KarmaPluginConfig.getInstance().getSqlDatabase());
            statement.executeUpdate();

            statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " + KarmaPluginConfig.getInstance().getPlayerTable() + "("
                            + "player_id BIGINT AUTO_INCREMENT not null, "
                            + "uuid VARCHAR(50) UNIQUE NOT NULL, "
                            + "effects_enabled BOOLEAN, "
                            + "effect VARCHAR(50), "
                            + "PRIMARY KEY (player_id));");
            statement.executeUpdate();

            statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " + KarmaPluginConfig.getInstance().getTransactionsTable() + "("
                            + "transaction_id BIGINT AUTO_INCREMENT NOT NULL, "
                            + "sender_id BIGINT, "
                            + "receiver_id BIGINT, "
                            + "time_stamp BIGINT, "
                            + "amount DOUBLE,"
                            + "FOREIGN KEY (sender_id) REFERENCES " + KarmaPluginConfig.getInstance().getPlayerTable() + "(player_id),"
                            + "FOREIGN KEY (receiver_id) REFERENCES " + KarmaPluginConfig.getInstance().getPlayerTable() + "(player_id),"
                            + "PRIMARY KEY (transaction_id));");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
