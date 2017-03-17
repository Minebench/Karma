package io.github.apfelcreme.Karma.Bungee.Exception;

import io.github.apfelcreme.Karma.Bungee.User.Transaction;

/**
 * Created by Max on 17.03.2017.
 */
public class InsaneKarmaAmountException extends Exception {
    private final Transaction transaction;

    /**
     * Gets called when a transaction has an transaction below 0 or above the configured karma transaction
     *
     * @param transaction The transaction it tries to set
     */
    public InsaneKarmaAmountException(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
