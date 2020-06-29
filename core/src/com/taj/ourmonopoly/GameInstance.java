package com.taj.ourmonopoly;

import java.io.IOException;
import java.util.ArrayList;

import com.taj.ourmonopoly.block.Block;
import com.taj.ourmonopoly.block.Property;

/**
 * The non-GUI representation of a game.
 */
public class GameInstance {

    // these are constants that blocks use to tell the instance to carry out a specific task
    public static final int TASK_NO_OP = 0;
    public static final int TASK_CREATE_PURCHASE_DIALOG = 1;
    public static final int TASK_CREATE_UPGRADE_DIALOG = 2;
    public static final int TASK_CREATE_PAY_RENT_DIALOG = 3;

    // public static final int 

    public static final int MAP_SIZE = 80;
    int startingCashAmt = 1500;
    int turn;
    GameScreen screen;
    /**
     * The list of players. Currently the game supports 4 players.
     */
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Dice> dice = new ArrayList<>();
    ArrayList<Block> blocks;

    /**
     * Creating a game instance.
     * 
     * @param names players' names
     */
    public GameInstance(GameScreen screen, String[] names) {
        this.screen = screen;
        try {
            blocks = Block.getBlockList(GameApp.PATH_TO_ASSETS + "blockData.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        this.turn = 0;
        for (int i = 0; i < 4; i++) {
            addPlayer(names[i]);
        }

        this.dice.add(new Dice());
        this.dice.add(new Dice());
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
        System.out.println("The dice roll is " + res);
        return res;
    }

    public void queryBlock(Player player, int pos) {
        int result = 0;
        if (pos < 53) {
            // do nothing
        }
        else if (pos < 63) {
            pos = 80 - pos;
        }
        else if (pos < 67) {
            pos = pos - 10;
        }
        else if (pos < 77) {
            pos = 90 - pos;
        }
        else {
            pos = pos - 20;
        }
        result = blocks.get(pos).interact(player);

        switch (result) {
            case TASK_NO_OP:
                break;
            case TASK_CREATE_PURCHASE_DIALOG:
                screen.createDialog("PurchaseProperty", blocks.get(pos), player);
                break;
            case TASK_CREATE_PAY_RENT_DIALOG:
                screen.createDialog("ShowAlert", player.name + " paid $" + ((Property) blocks.get(pos))
                    .getCurrentRent() + " to " + ((Property) blocks.get(pos)).owner.name + ".");
                break;
            default:
                break;
        }
    }
}