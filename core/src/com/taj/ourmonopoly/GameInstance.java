package com.taj.ourmonopoly;

import java.io.IOException;
import java.util.ArrayList;

import com.taj.ourmonopoly.block.Block;
import com.taj.ourmonopoly.block.Hospital;
import com.taj.ourmonopoly.block.Jail;
import com.taj.ourmonopoly.block.Property;

/**
 * The non-GUI representation of a game.
 */
public class GameInstance {

    public static enum Task {
        NO_OP, CREATE_PURCHASE_DIALOG, CREATE_UPGRADE_DIALOG,
        PAY_RENT, METRO, PAY_HUNDRED, RECIEVE_FIFTY, GO_TO_JAIL, CREATE_JAIL_DIALOG, SPLIT_CASH, GO_TO_HOSPITAL, CREATE_HOSPITAL_DIALOG
    }

    // public static final int 

    public static final int MAP_SIZE = 80;
    int startingCashAmt = 1500;
    int turn;
    boolean isDouble;
    GameScreen screen;

    /**
     * The list of players. Currently the game supports 4 players.
     */
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Dice> dice = new ArrayList<>();
    ArrayList<Block> blocks;
    private int lastDiceRoll = -1;

    // convenience variables
    Jail jail;
    Hospital hospital;


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

        for (var b : blocks) {
            if (b instanceof Jail)
                jail = (Jail) b;
            else if (b instanceof Hospital)
                hospital = (Hospital) b;
        }
        
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

        hospital.accept(players.get(0));
        // players.get(0).purchaseProperty(((Property) blocks.get(1)));
        // players.get(0).purchaseProperty(((Property) blocks.get(2)));
        // players.get(0).purchaseProperty(((Property) blocks.get(3)));

    }

    public void addPlayer(String name) {
        players.add(new Player(this, name, players.size(), startingCashAmt));
    }

    public void nextPlayer() {
        nextPlayer(true);
    }

    private void nextPlayer(boolean next) {
        nextPlayer(next, true);
    }

    private void nextPlayer(boolean next, boolean newDiceRoll) {
        if (next) {
            turn = (turn + 1) % players.size();
        }
        var p = players.get(turn);
        p.move(newDiceRoll ? getDiceRoll() : lastDiceRoll);
        queryBlock(p);
    }

    /**
     * Queries the appropriate block for action.
     * 
     * @param pos the position of the block (after conversion from
     *            {@link Player#position})
     */
    private void queryBlock(Player player) {
        int pos = convertPos(player.getPosition());
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
                break;
            case PAY_HUNDRED:
                screen.createDialog("ShowAlert", player.name + " paid $100.");
                break;
            case RECIEVE_FIFTY:
                screen.createDialog("ShowAlert", player.name + " recieved $50.");
                break;
            case GO_TO_JAIL:
                screen.createDialog("ShowAlert", "Oops! To jail...");
                jail.accept(player);
                break;
            case GO_TO_HOSPITAL:
                screen.createDialog("ShowAlert", "Oops! To hospital...");
                hospital.accept(player);
                break;
            case SPLIT_CASH:
                screen.createDialog("ShowAlert", "Every player's cash is equally redistributed!");
                splitCash();
                break;
            case CREATE_JAIL_DIALOG:
                screen.createDialog("Jail", player);
                break;
            case CREATE_HOSPITAL_DIALOG:
                screen.createDialog("Hospital", player);
                break;
            default:
                break;
        }
    }

    public void payAndRelease(Player player) {
        player.pay(50);
        jail.release(player);
        nextPlayer(false);
    }

    public void payAndCure(Player player) {
        player.pay(100);
        hospital.release(player);
        nextPlayer(false);
    }

    public void tryToReleaseFromJail(Player player) {
        getDiceRoll();
        if (isDouble) {
            jail.release(player);
            nextPlayer(false, false);
        }
        else if (player.immobilized == 1) {
            screen.createDialog("ShowAlert", "Oops! No double this time. You have to pay now.");
            payAndRelease(player);
        }
        else {
            screen.createDialog("ShowAlert", "Oops! No double this time.");
        }
    }
    
    public void tryToReleaseFromHospital(Player player) {
        int roll = getDiceRoll();
        if (roll <= 5) {
            hospital.release(player);
            nextPlayer(false, false);
        }
        else if (player.immobilized == 1) {
            screen.createDialog("ShowAlert", "Oops! Not enough luck this time. You have to pay now.");
            payAndCure(player);
        }
        else {
            screen.createDialog("ShowAlert", "Oops! Not enough luck this time.");
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

    private void splitCash() {
        int cashSum = players
            .stream()
            .mapToInt(Player::getCashAmt)
            .sum();
        for (var p : players) {
            p.setCashAmt(cashSum / players.size());
        }
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
            pos = pos - 26;
        }
        else if (pos < 67) {
            pos = pos - 10;
        }
        else if (pos < 77) {
            pos = pos - 54;
        }
        else {
            pos = pos - 20;
        }

        return pos;
    }

    private int getDiceRoll() {
        int roll1, roll2;
        roll1 = dice.get(0).next();
        roll2 = dice.get(1).next();
        isDouble = roll1 == roll2;
        lastDiceRoll = roll1 + roll2;
        System.out.println("The dice roll is " + roll1 + " and " + roll2);
        return roll1 + roll2;
    }

}