package io.github.apfelcreme.Karma.Bungee.User;

import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.Particle.Effect;

import java.util.*;

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
public class PlayerData {

    private UUID uuid;
    private Effect effect;
    private Boolean effectsEnabled;
    private List<Transaction> transactionsDone;
    private List<Transaction> transactionsReceived;

    public PlayerData(UUID uuid, Effect effect, Boolean effectsEnabled, List<Transaction> transactionsDone, List<Transaction> transactionsReceived) {
        this.uuid = uuid;
        this.effect = effect;
        this.effectsEnabled = effectsEnabled;
        this.transactionsDone = transactionsDone;
        this.transactionsReceived = transactionsReceived;
    }

    /**
     * saves the playerdata
     */
    public void save() {
        KarmaPlugin.getInstance().getDatabaseController().insertPlayerData(this);
    }

    /**
     * returns the players uuid
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * returns this players relation to another player
     *
     * @param to the other player
     * @return the relation between the two from the pov of this player
     */
    public Relation getRelation(UUID to) {
        if (to != null) {
            List<Transaction> doneRelationTransactions = new ArrayList<>();
            List<Transaction> receivedRelationTransactions = new ArrayList<>();
            for (Transaction doneTransaction : transactionsDone) {
                if (doneTransaction.getReceiver().equals(to)) {
                    doneRelationTransactions.add(doneTransaction);
                }
            }
            for (Transaction receivedTransaction : transactionsReceived) {
                if (receivedTransaction.getSender().equals(to)) {
                    receivedRelationTransactions.add(receivedTransaction);
                }
            }
            return new Relation(to, doneRelationTransactions, receivedRelationTransactions);
        }
        return null;
    }

    /**
     * returns a map of all relations a player has
     *
     * @return a map of relations
     */
    public Map<UUID, Relation> getRelations() {
        Map<UUID, Relation> relations = new HashMap<>();
        for (Transaction transaction : transactionsDone) {
            Relation relation = relations.get(transaction.getReceiver());
            if (relation == null) {
                relation = new Relation(transaction.getReceiver(), new ArrayList<Transaction>(), new ArrayList<Transaction>());
                relations.put(transaction.getReceiver(), relation);
            }
            relation.getTransactionsDoneTo().add(transaction);
        }
        for (Transaction transaction : transactionsReceived) {
            Relation relation = relations.get(transaction.getSender());
            if (relation == null) {
                relation = new Relation(transaction.getSender(), new ArrayList<Transaction>(), new ArrayList<Transaction>());
                relations.put(transaction.getSender(), relation);
            }
            relation.getTransactionsReceivedFrom().add(transaction);
        }
        return relations;
    }

    /**
     * returns the amount of karma the player currently has
     *
     * @return the amount of karma the player currently has
     */
    public double getKarma() {
        double karma = 0.0;
        for (Transaction transaction : transactionsReceived) {
            karma += transaction.getAmount();
        }
        return karma;
    }

    /**
     * returns the list of transactions the player has done
     *
     * @return the list of transactions the player has done
     */
    public List<Transaction> getTransactionsDone() {
        return transactionsDone;
    }

    /**
     * returns the list of transactions the player has received
     *
     * @return the list of transactions the player has received
     */
    public List<Transaction> getTransactionsReceived() {
        return transactionsReceived;
    }

    /**
     * returns the effect the player is currently using
     *
     * @return the effect the player is currently using
     */
    public Effect getEffect() {
        return effect;
    }

    /**
     * sets a players effect
     *
     * @param effect the effect
     */
    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    /**
     * returns whether the player is seeing his or other players particles
     *
     * @return true or false
     */
    public boolean effectsEnabled() {
        return effectsEnabled;
    }


    @Override
    public String toString() {
        return "PlayerData{" +
                "uuid=" + uuid + "(" + KarmaPlugin.getInstance().getNameByUUID(uuid) + ")" +
                ", effect=" + effect +
                ", effectsEnabled=" + effectsEnabled +
                ", transactionsDone=" + transactionsDone +
                ", transactionsReceived=" + transactionsReceived +
                '}';
    }

}
