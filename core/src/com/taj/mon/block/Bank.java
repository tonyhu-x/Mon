package com.taj.mon.block;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.taj.mon.Player;
import com.taj.mon.GameInstance.Task;

public class Bank extends SqrBlock {

    public static class Transaction {
        private Player player;

        /**
         * A positive amount represents a deposit. A negative amount represents a
         * vithdrawal.
         */
        private int amount;

        public Transaction(Player player, int amount) {
            this.player = player;
            this.amount = amount;
        }

        /**
         * Applies the transaction.
         * 
         * @return {@code true} if the transaction is successfully applied.
         */
        private boolean apply() {
            if (amount > 0) {
                if (player.cashAmt < amount) {
                    return false;
                }
                player.cashAmt -= amount;
                player.savings += amount;
            }
            else {
                if (player.savings < -amount) {
                    return false;
                }
                player.cashAmt -= amount;
                player.savings += amount;
            }
            return true;
        }

        public String getType() {
            return amount > 0 ? "Deposit" : "Withdrawal";
        }

        public int getAmount() {
            return amount;
        }
    } 

    private static ArrayList<Transaction> transactions = new ArrayList<>();
    
    /**
     * @return {@code true} when at least one transaction is successfully processed
     */
    public static boolean processTransactions(Player player) {
        var iter = transactions.iterator();
        var res = false;
        while (iter.hasNext()) {
            var t = iter.next();
            if (t.player == player) {
                if (!res)
                    res = t.apply();
                iter.remove();
            }
        }
        return res;
    }

    public static ArrayList<Transaction> getTransactions(Player player) {
        return transactions
            .stream()
            .filter(t -> t.player == player)
            .collect(Collectors.toCollection(ArrayList<Transaction>::new));
    }

    /**
     * Refreshes the player's bank status (e.g. pays interest).
     */
    public static void refresh(Player player) {
        player.savings *= 1.05;
    }

    public static void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Bank(String name, int index) {
        super(name, index);
    }

    @Override
    public Task interact(Player player) {
        return Task.NO_OP;
    }

    @Override
    public String getTextureName() {
        return "bank";
    }
}