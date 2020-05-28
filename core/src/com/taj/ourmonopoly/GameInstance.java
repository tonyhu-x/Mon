package com.taj.ourmonopoly;

import java.util.ArrayList;

public class GameInstance {

    int startingCashAmt;
    int turn;

    /**
     * A list of players. Currently the game supports 4 players.
     */
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Dice> dice = new ArrayList<>();

    public GameInstance() {
        this.turn = 0;
        for (int i = 0; i < 4; i++) {
            addPlayer("fill in the blank");
        }
    }
    
    public void addPlayer(String name) {
        players.add(new Player(name, startingCashAmt));
    }
}