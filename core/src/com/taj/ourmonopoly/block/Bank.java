package com.taj.ourmonopoly.block;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.taj.ourmonopoly.GameInstance.Task;
import com.taj.ourmonopoly.Player;

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

        private void apply() {
            if (amount > 0) {
                if (player.cashAmt < amount) {
                    return;
                }
                player.cashAmt -= amount;
                player.savings += amount;
            }
            else {
                if (player.savings < -amount) {
                    return;
                }
                player.cashAmt -= amount;
                player.savings += amount;
            }
        }

        public String getType() {
            return amount > 0 ? "Deposit" : "Withdrawal";
        }

        public int getAmount() {
            return amount;
        }
    } 

    private static ArrayList<Transaction> transactions = new ArrayList<>();
    
    public static void processTransactions(Player player) {
        var iter = transactions.iterator();
        while (iter.hasNext()) {
            var t = iter.next();
            if (t.player == player) {
                t.apply();
                iter.remove();
            }
        }
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