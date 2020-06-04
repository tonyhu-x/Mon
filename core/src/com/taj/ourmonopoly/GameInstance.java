package com.taj.ourmonopoly;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.taj.ourmonopoly.block.Block;

public class GameInstance {

    int startingCashAmt;
    int turn;

    /**
     * The list of players. Currently the game supports 4 players.
     */
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Dice> dice = new ArrayList<>();

    public GameInstance() {
        try {
            Block.getBlockList(GameApp.PATH_TO_ASSETS + "mapData.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void nextPlayer() {
        turn = (turn + 1) % players.size();
        players.get(turn).move(getDiceRoll());
    }

    public int getDiceRoll() {
        int res = 0;
        for (var d : dice) {
            res += d.next();
        }
        return res;
    }
}