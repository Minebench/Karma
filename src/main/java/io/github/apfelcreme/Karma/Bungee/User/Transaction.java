package io.github.apfelcreme.Karma.Bungee.User;

import io.github.apfelcreme.Karma.Bungee.Exception.OncePerDayException;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;

import java.util.ArrayList;
import java.util.Date;
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
public class Transaction {

    /**
     * a day has 86400000 milliseconds
     */
    private static final int MILLISECONDS_PER_DAY = 86400000;

    private int id;
    private UUID sender;
    private UUID receiver;
    private Double amount;
    private Date date;

    public Transaction(int id, UUID sender, UUID receiver, Double amount, Date date) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.date = date;
    }

    public Transaction(UUID sender, UUID receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = new Date();
    }

    /**
     * saves the transaction
     */
    public void save() throws OncePerDayException {
        PlayerData senderData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(sender);
        if (senderData == null) {
            senderData = new PlayerData(sender, null, true, new ArrayList<Transaction>(), new ArrayList<Transaction>());
            senderData.save();
        }

        PlayerData receiverData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(receiver);
        if (receiverData == null) {
            receiverData = new PlayerData(receiver, null, true, new ArrayList<Transaction>(), new ArrayList<Transaction>());
            receiverData.save();
        }

        if ((senderData.getRelation(receiver) != null)
                && (new Date().getTime() < (senderData.getRelation(receiver).getLastTransactionDate().getTime() + MILLISECONDS_PER_DAY))) {
            throw new OncePerDayException(senderData.getRelation(receiver));
        }
        amount = KarmaPluginConfig.getInstance().getConfiguration().getDouble("karmaPerThx") * senderData.getRelation(receiver).getRatio();
        id = KarmaPlugin.getInstance().getDatabaseController().insertTransaction(this);
        KarmaPlugin.getInstance().getLogger().info("Transaction (" + sender + " -> " + receiver + " = " + amount + ") saved!");

        senderData.getTransactionsDone().add(this);
        receiverData.getTransactionsReceived().add(this);
    }

    /**
     * deletes the transaction
     */
    public void delete() {
        PlayerData senderData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(sender);
        if (senderData == null) {
            senderData = new PlayerData(sender, null, true, new ArrayList<Transaction>(), new ArrayList<Transaction>());
            senderData.save();
        }

        PlayerData receiverData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(receiver);
        if (receiverData == null) {
            receiverData = new PlayerData(receiver, null, true, new ArrayList<Transaction>(), new ArrayList<Transaction>());
            receiverData.save();
        }
        KarmaPlugin.getInstance().getDatabaseController().deleteTransaction(this);

        senderData.getTransactionsDone().remove(this);
        receiverData.getTransactionsReceived().remove(this);
    }

    /**
     * returns the transaction id
     *
     * @return the transaction id
     */
    public int getId() {
        return id;
    }

    /**
     * returns the uuid of the sender
     *
     * @return the uuid of the sender
     */
    public UUID getSender() {
        return sender;
    }

    /**
     * returns the uuid of the receiver
     *
     * @return the uuid of the receiver
     */
    public UUID getReceiver() {
        return receiver;
    }

    /**
     * returns the amount of karma that was sent
     *
     * @return the amount of karma that was sent
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * returns the date of the transaction
     *
     * @return the date of the transaction
     */
    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
