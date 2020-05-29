package com.taj.ourmonopoly;

import java.util.ArrayList;

public class GameInstance {

    int startingCashAmt;
    int turn;

    /**
     * The list of players. Currently the game supports 4 players.
     */
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Dice> dice = new ArrayList<>();

    public GameInstance() {
        this.turn = 0;
        for (int i = 0; i < 4; i++) {
            addPlayer("fill in the blank");
        }

        // determine the first player
        for (Player p : players) {
            if ((p.lastDiceRoll = getDiceRoll()) > players.get(turn).lastDiceRoll) {
                turn = p.number;
            }
        }

    }

    public void addPlayer(String name) {
        players.add(new Player(name, players.size(), startingCashAmt));
    }

    public int getDiceRoll() {
        int res = 0;
        for (var d : dice) {
            res += d.next();
        }
        return res;
    }
}