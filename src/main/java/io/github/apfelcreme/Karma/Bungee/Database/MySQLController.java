package io.github.apfelcreme.Karma.Bungee.Database;

import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.Particle.Effect;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import io.github.apfelcreme.Karma.Bungee.User.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
public class MySQLController implements DatabaseController {

    /**
     * selects a players userdata. only call asnyc
     *
     * @param uuid the players uuid
     */
    @Override
    public PlayerData getPlayerData(UUID uuid) {
        if (KarmaPlugin.getInstance().getPlayerDataCache().containsKey(uuid)) {
            return KarmaPlugin.getInstance().getPlayerDataCache().get(uuid);
        } else {
            Connection connection = MySQLConnector.getInstance().getConnection();
            if (connection != null) {
                try {
                    PlayerData playerData;
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT t.transaction_id, s.uuid as senderUUID, r.uuid as receiverUUID, t.time_stamp, t.amount " +
                                    "FROM " + KarmaPluginConfig.getInstance().getTransactionsTable() + " t " +
                                    " LEFT JOIN " + KarmaPluginConfig.getInstance().getPlayerTable() + " s on t.sender_id = s.player_id " +
                                    " LEFT JOIN " + KarmaPluginConfig.getInstance().getPlayerTable() + " r on t.receiver_id = r.player_id " +
                                    "WHERE s.uuid = ? or r.uuid = ?");
                    statement.setString(1, uuid.toString());
                    statement.setString(2, uuid.toString());
                    ResultSet resultSet = statement.executeQuery();
                    List<Transaction> doneTransactions = new ArrayList<>();
                    List<Transaction> receivedTransactions = new ArrayList<>();
                    while (resultSet.next()) {
                        Transaction transaction = new Transaction(
                                resultSet.getInt("transaction_id"),
                                UUID.fromString(resultSet.getString("senderUUID")),
                                UUID.fromString(resultSet.getString("receiverUUID")),
                                resultSet.getDouble("amount"),
                                resultSet.getLong("time_stamp"));
                        if (transaction.getSender().equals(uuid)) {
                            doneTransactions.add(transaction);
                        } else {
                            receivedTransactions.add(transaction);
                        }
                    }

                    statement = connection.prepareStatement(
                            "SELECT * FROM " + KarmaPluginConfig.getInstance().getPlayerTable() + " WHERE uuid = ?");
                    statement.setString(1, uuid.toString());
                    resultSet = statement.executeQuery();
                    if (resultSet.first()) {
                        // player data was found
                        playerData = new PlayerData(
                                uuid,
                                Effect.getEffect(resultSet.getString("effect")) != null ?
                                        Effect.getEffect(resultSet.getString("effect")) : Effect.NONE,
                                resultSet.getBoolean("effects_enabled"),
                                doneTransactions,
                                receivedTransactions
                        );
                        KarmaPlugin.getInstance().getPlayerDataCache().put(uuid, playerData);
                        return playerData;
                    } else {
                        // player did not exist -> create
                        playerData = new PlayerData(uuid, null, true, new ArrayList<Transaction>(), new ArrayList<Transaction>());
                        playerData.save();
                        return playerData;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    MySQLConnector.getInstance().closeConnection(connection);
                }
            }
        }
        return null;
    }

    /**
     * inserts a transaction
     *
     * @param transaction the transaction that is being inserted
     * @return the transaction id
     */
    @Override
    public Integer insertTransaction(Transaction transaction) {
        Connection connection = MySQLConnector.getInstance().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + KarmaPluginConfig.getInstance().getTransactionsTable() +
                                "(sender_id, receiver_id, time_stamp, amount) " +
                                "VALUES (" +
                                " (Select player_id from " + KarmaPluginConfig.getInstance().getPlayerTable() + " where uuid = ?), " +
                                " (Select player_id from " + KarmaPluginConfig.getInstance().getPlayerTable() + " where uuid = ?), " +
                                "  ? , " +
                                "  ?);",
                        Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, transaction.getSender().toString());
                statement.setString(2, transaction.getReceiver().toString());
                statement.setLong(3, transaction.getTime());
                statement.setDouble(4, transaction.getAmount());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MySQLConnector.getInstance().closeConnection(connection);
            }
        }
        return null;
    }

    /**
     * deletes a transaction
     *
     * @param transaction the transaction that shall be deleted
     */
    @Override
    public void deleteTransaction(Transaction transaction) {
        Connection connection = MySQLConnector.getInstance().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM " + KarmaPluginConfig.getInstance().getTransactionsTable() +
                                " WHERE transaction_id = ?");
                statement.setInt(1, transaction.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MySQLConnector.getInstance().closeConnection(connection);
            }
        }
    }

    /**
     * inserts a players data
     *
     * @param playerData the playerData that is being inserted
     * @return the player id
     */
    @Override
    public Integer insertPlayerData(PlayerData playerData) {
        Connection connection = MySQLConnector.getInstance().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection
                        .prepareStatement("INSERT INTO " + KarmaPluginConfig.getInstance().getPlayerTable() +
                                        "(uuid, effect, effects_enabled) " +
                                        "VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE effect = ?, effects_enabled = ?",
                                Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, playerData.getUuid().toString());
                statement.setString(2, playerData.getEffect() != null ? playerData.getEffect().name() : null);
                statement.setBoolean(3, playerData.effectsEnabled());
                statement.setString(4, playerData.getEffect() != null ? playerData.getEffect().name() : null);
                statement.setBoolean(5, playerData.effectsEnabled());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MySQLConnector.getInstance().closeConnection(connection);
            }
        }
        return null;
    }

    /**
     * returns the n highest ranking players
     *
     * @param size the number of displayed players
     * @return a sorted list
     */
    @Override
    public List<PlayerData> getTopList(Integer size) {
        List<PlayerData> topList = new ArrayList<>(size);
        Connection connection = MySQLConnector.getInstance().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection
                        .prepareStatement("SELECT SUM(t.amount) as amount, p.uuid " +
                                "FROM " + KarmaPluginConfig.getInstance().getTransactionsTable() + " t " +
                                "LEFT JOIN " + KarmaPluginConfig.getInstance().getPlayerTable() + " p ON t.receiver_id = p.player_id " +
                                "GROUP BY receiver_id ORDER BY amount DESC LIMIT " + size);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    topList.add(getPlayerData(UUID.fromString(resultSet.getString("uuid"))));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MySQLConnector.getInstance().closeConnection(connection);
            }
        }
        return topList;
    }
}
