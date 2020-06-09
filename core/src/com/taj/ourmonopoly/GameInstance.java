package com.taj.ourmonopoly;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.taj.ourmonopoly.block.Block;

/**
 * The non-GUI representation of a game.
 */
public class GameInstance {

    public static final int MAP_SIZE = 80;
    int startingCashAmt;
    int turn;
    
    /**
     * The list of players. Currently the game supports 4 players.
     */
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Dice> dice = new ArrayList<>();
    ArrayList<Block> blocks;

    public GameInstance() {
        try {
            blocks = Block.getBlockList(GameApp.PATH_TO_ASSETS + "blockData.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
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
        players.add(new Player(this, name, players.size(), startingCashAmt));
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

    public void queryBlock(Player player, int pos) {
        if (pos < 53) {
            blocks.get(pos).interact(player);
        }
        else if (pos < 63) {
            blocks.get(80 - pos).interact(player);
        }
        else if (pos < 67) {
            blocks.get(pos - 10).interact(player);
        }
        else if (pos < 77) {
            blocks.get(90 - pos).interact(player);
        }
        else {
            blocks.get(pos - 20).interact(player);
        }
    }
}