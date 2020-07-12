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
    public static final int TASK_PAY_RENT = 3;
    public static final int TASK_METRO = 4;
    
    public static enum Task {
        NO_OP, CREATE_PURCHASE_DIALOG, CREATE_UPGRADE_DIALOG,
        PAY_RENT, METRO
    }

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
     * Creates a game instance.
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

        players.get(0).purchaseProperty(((Property) blocks.get(1)));
        players.get(0).purchaseProperty(((Property) blocks.get(2)));
        // players.get(0).purchaseProperty(((Property) blocks.get(3)));

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

    /**
     * Queries the appropriate block for action.
     * 
     * @param pos the position of the block (after conversion from
     *            {@link Player#position})
     */
    public void queryBlock(Player player, int pos) {
        Task result = blocks.get(pos).interact(player);

        switch (result) {
            case NO_OP:
                break;
            case CREATE_PURCHASE_DIALOG:
                screen.createDialog("PurchaseProperty", blocks.get(pos), player);
                break;
            case PAY_RENT:
                int rent = calcRent(pos);
                player.payTo(((Property) blocks.get(pos)).owner, rent);
                screen.createDialog("ShowAlert",
                        player.name + " paid $" + rent + " to " + ((Property) blocks.get(pos)).owner.name + ".");
                break;
            case METRO:
                screen.createDialog("Metro", blocks.get(pos), player);
            default:
                break;
        }
    }

    
    private int calcRent(int pos) {
        int oldPos = pos;
        int rent = ((Property) blocks.get(pos)).getCurrentRent();
        Property origin = (Property) blocks.get(pos);
        Block b = blocks.get(--pos);
        while (b instanceof Property && ((Property) b).getGroup() == origin.getGroup()) {
            if (((Property) b).owner == origin.owner) {
                rent += ((Property) b).getCurrentRent() / 2;
            }
            b = blocks.get(--pos);
        }
        pos = oldPos;
        b = blocks.get(++pos);

        while (b instanceof Property && ((Property) b).getGroup() == origin.getGroup()) {
            if (((Property) b).owner == origin.owner) {
                rent += ((Property) b).getCurrentRent() / 2;
            }
            b = blocks.get(++pos);
        }

        return rent;
    }

    public int getBlockGroup(int pos) {
        Block b = blocks.get(pos);
        if (b instanceof Property) {
            return ((Property) b).getGroup();
        }
        return 0;
    }

    public Player getCurrentPlayer() {
        return players.get(turn);
    }

    /**
     * Checks if the player has a monopoly on the specified group.
     */
    public boolean isMonopoly(Player player, int group) {
        return blocks
                .stream()
                .filter(
                    (block) ->
                        (block instanceof Property
                        && ((Property) block).getGroup() == group
                        && ((Property) block).owner != player)
                ).count() == 0;
    }

    /**
     * Converts player position to map position.
     * 
     * @param pos player position
     * @return the converted position
     */
    public int convertPos(int pos) {
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

        return pos;
    }
}