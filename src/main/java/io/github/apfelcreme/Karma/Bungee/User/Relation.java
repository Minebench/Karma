package io.github.apfelcreme.Karma.Bungee.User;

import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;

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
public class Relation implements Comparable<Relation> {

    /**
     * a day has 86400000 milliseconds
     */
    private static final int MILLISECONDS_PER_DAY = 86400000;

    private UUID to;
    private List<Transaction> transactionsDoneTo;
    private List<Transaction> transactionsReceivedFrom;

    protected Relation(UUID receiver, List<Transaction> transactionsDoneTo, List<Transaction> transactionsReceivedFrom) {
        this.to = receiver;
        this.transactionsDoneTo = transactionsDoneTo;
        this.transactionsReceivedFrom = transactionsReceivedFrom;
    }

    /**
     * returns the ratio that describes the relation between the two players.
     * The more karma the sender gives to the to the lower the amount of karma he sends becomes.
     * This will also take into consideration the frequency that the karma was given in with the
     * goal to simulate the regeneration of one /thx per week.
     *
     * @return the karma multiplier that is used when the sender sends karma to the to
     */
    public double getRatio() {
        if (transactionsDoneTo.isEmpty()) {
            return 1;
        }

        int regenerationDays = KarmaPluginConfig.getInstance().getConfiguration().getInt("karmaRegenerationDays");
        if (regenerationDays > 0) {
            // decline/regeneration = (0.4*e)^(-regenerate * (regenerate - days since last transaction))
            double daysSinceLastTransaction = (System.currentTimeMillis() - getLastTransactionTime()) / MILLISECONDS_PER_DAY;
            double ratio = Math.pow(0.4 * Math.E, -regenerationDays * (regenerationDays - daysSinceLastTransaction));
            return ratio > 1 ? 1 : ratio;
        } else {
            // ratio = (0.4*e)^(-amount of transactions)
            return Math.pow(0.4 * Math.E, -1 * transactionsDoneTo.size());
        }
    }

    /**
     * returns the to
     *
     * @return the receiving player
     */
    public UUID getTo() {
        return to;
    }

    /**
     * returns the list of transaction the player has sent
     *
     * @return the list of transaction the player has sent
     */
    public List<Transaction> getTransactionsDoneTo() {
        return transactionsDoneTo;
    }

    /**
     * returns the list of transaction the player has received
     *
     * @return the list of transaction the player has received
     */
    public List<Transaction> getTransactionsReceivedFrom() {
        return transactionsReceivedFrom;
    }

    /**
     * returns the amount of karma that was given to the other player
     *
     * @return the amount of karma that was given to the other player
     */
    public double getAmountGiven() {
        double amount = 0.0;
        for (Transaction transaction : transactionsDoneTo) {
            amount += transaction.getAmount();
        }
        return amount;
    }

    /**
     * returns the amount of karma that was received from the other player
     *
     * @return the amount of karma that was received from the other player
     */
    public double getAmountReceived() {
        double amount = 0.0;
        for (Transaction transaction : transactionsReceivedFrom) {
            amount += transaction.getAmount();
        }
        return amount;
    }

    /**
     * returns the date of the first transaction the player sent to the receiver
     *
     * @return the date of the first transaction done by the relation sender;
     */
    public long getFirstTransactionTime() {
        long time = System.currentTimeMillis();
        for (Transaction transaction : transactionsDoneTo) {
            if (transaction.getTime() < time) {
                time = transaction.getTime();
            }
        }
        return time;
    }

    /**
     * returns the date of the last transaction the player sent to the receiver
     *
     * @return the date of the last transaction done by the relation sender;
     */
    public long getLastTransactionTime() {
        long time =0;
        for (Transaction transaction : transactionsDoneTo) {
            if (transaction.getTime() > time) {
                time = transaction.getTime();
            }
        }
        return time;
    }

    @Override
    public String toString() {
        return "\n  Relation{" +
                "to=" + to +
                ", transactionsDoneTo=" + transactionsDoneTo +
                ", transactionsReceivedFrom=" + transactionsReceivedFrom +
                '}';
    }

    /**
     * compares this relation to another relation
     *
     * @param o another relation
     * @return -1 if smaller, 0 if equal, +1 if greater
     */
    @Override
    public int compareTo(Relation o) {
        return Double.compare(o.getAmountGiven(), this.getAmountGiven());

    }
}
