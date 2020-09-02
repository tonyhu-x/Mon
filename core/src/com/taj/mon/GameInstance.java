package com.taj.mon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.taj.mon.block.Bank;
import com.taj.mon.block.Block;
import com.taj.mon.block.Hospital;
import com.taj.mon.block.Jail;
import com.taj.mon.block.Property;
import com.taj.mon.dialog.AlertActionDialog.AlertAction;

/**
 * The non-GUI representation of a game.
 */
public class GameInstance {

    public static enum Task {
        NO_OP, CREATE_PURCHASE_DIALOG, CREATE_UPGRADE_DIALOG,
        PAY_RENT, METRO, PAY_HUNDRED, RECEIVE_FIFTY, GO_TO_JAIL, CREATE_JAIL_DIALOG, SPLIT_CASH, GO_TO_HOSPITAL, CREATE_HOSPITAL_DIALOG, ADVANCE_TO_GO, BACK_TWO
    }

    // public static final int 

    public static final int MAP_SIZE = 80;
    public static final int INITIAL_CASH_AMT = 3000;
    int turn;
    boolean isDouble;
    GameScreen screen;

    /**
     * The list of players.
     */
    public ArrayList<Player> players = new ArrayList<>();
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
            blocks = Block.getBlockList("blockData.txt");
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
        
        for (var name : names) {
            addPlayer(name);
        }

    }

    /**
     * Called at the end of {@link GameScreen#show()}.
     */
    public void onShow() {
        screen.createDialog("AlertAction", "Who's going first?", (AlertAction) () ->
            screen.requestDiceRoll(new Consumer<List<Integer>>() {
                private int which = 0;

                @Override
                public void accept(List<Integer> list) {
                    if ((players.get(which).lastDiceRoll = diceRollSum(list)) > players.get(turn).lastDiceRoll) {
                        turn = which;
                    }
                    if (which != players.size() - 1) {
                        which++;
                        screen.requestDiceRoll(this, false);
                    }
                    else {
                        screen.createDialog("ShowAlert", players.get(turn).name + " will go first.");
                        turn = turn == 0 ? players.size() - 1 : turn - 1;
                    }
                }
            }, false)
        );
    }
    
    public void addPlayer(String name) {
        players.add(new Player(this, name, players.size(), INITIAL_CASH_AMT));
    }
    
    public void nextPlayer() {
        nextPlayer(true);
    }
    
    private void nextPlayer(boolean next) {
        nextPlayer(next, true);
    }
    
    private void nextPlayer(boolean next, boolean newDiceRoll) {
        Player p;
        if (next) {
            players.get(turn).turnEnd();
            turn = (turn + 1) % players.size();
            p = players.get(turn);
            p.turnStart();
        }
        else {
            p = players.get(turn);
        }
        if (p.immobilized > 0) {
            queryBlock(p);
        }
        if (newDiceRoll) {
            screen.requestDiceRoll(d -> {
                p.move(diceRollSum(d));
                queryBlock(p);
            }, true);
        }
        else {
            p.move(lastDiceRoll);
            queryBlock(p);
        }
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
                screen.createDialog("ShowAlert",
                    player.name + " paid $" + rent + " to " + ((Property) blocks.get(pos)).owner.name + ".");
                player.payTo(((Property) blocks.get(pos)).owner, rent);
                break;
            case METRO:
                screen.createDialog("Metro", blocks.get(pos), player);
                break;
            case PAY_HUNDRED:
                player.payTo(null, 100);
                screen.createDialog("ShowAlert", player.name + " paid $100.");
                break;
            case RECEIVE_FIFTY:
                player.receive(50);
                screen.createDialog("ShowAlert", player.name + " received $50.");
                break;
            case GO_TO_JAIL:
                screen.createDialog("AlertAction", "Oops! To jail...", (AlertAction) () -> jail.accept(player));
                break;
            case GO_TO_HOSPITAL:
                screen.createDialog("AlertAction", "Oops! To hospital...", (AlertAction) () -> hospital.accept(player));
                break;
            case ADVANCE_TO_GO:
                screen.createDialog("AlertAction", "Advancing to Go...", (AlertAction) () -> player.forward(MAP_SIZE - player.getPosition()));
                break;
            case SPLIT_CASH:
                screen.createDialog("ShowAlert", "Every player's cash is equally redistributed!");
                splitCash();
                break;
            case BACK_TWO:
                screen.createDialog("AlertAction", "Going back two spaces...", (AlertAction) () -> {
                    player.backward(2);
                    GameInstance.this.queryBlock(player);
                });
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
        player.payTo(null, 50);
        jail.release(player);
        nextPlayer(false);
    }

    public void payAndCure(Player player) {
        player.payTo(null, 100);
        hospital.release(player);
        nextPlayer(false);
    }

    public void tryToReleaseFromJail(Player player) {
        screen.requestDiceRoll(d -> {
            // is double or not
            if (d.get(0).equals(d.get(1))) {
                jail.release(player);
                // set lastDiceRoll
                diceRollSum(d);
                nextPlayer(false, false);
            }
            else if (player.immobilized == 1) {
                screen.createDialog("ShowAlert", "Oops! No double this time. You have to pay now.");
                payAndRelease(player);
            }
            else {
                screen.createDialog("ShowAlert", "Oops! No double this time.");
            }
        }, false);
    }

    public void tryToReleaseFromHospital(Player player) {
        screen.requestDiceRoll(d -> {
            if (diceRollSum(d) <= 5) {
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
        }, false);
    }

    // it is assumed that the parameters are correct
    public void trade(Player p1, Player p2,
                        ArrayList<Property> pro1, ArrayList<Property> pro2, int amt)
    {
        if (amt > 0) {
            p1.payTo(p2, amt);
        }
        else {
            p2.payTo(p1, -amt);
        }
        pro1.forEach(p -> p1.transferProperty(p, p2));
        pro2.forEach(p -> p2.transferProperty(p, p1));
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
        if (oldPos == blocks.size() - 1) {
            return rent;
        }
        pos = oldPos;
        b = blocks.get(++pos);

        while (b instanceof Property && ((Property) b).getGroup() == origin.getGroup()) {
            if (((Property) b).owner == origin.owner) {
                rent += ((Property) b).getCurrentRent() / 2;
            }
            if (pos == blocks.size() - 1) {
                return rent;
            }
            b = blocks.get(++pos);
        }

        return rent;
    }

    private void splitCash() {
        int cashSum = players
            .stream()
            .mapToInt(p -> p.cashAmt)
            .sum();
        for (var p : players) {
            p.cashAmt = cashSum / players.size();
        }
    }

    public void bankruptCheck(Player player) {
        int target = -player.cashAmt;
        screen.createDialog("ShowAlert", "You are about to go bankrupt. You need to raise $" + target + ".");
        screen.waitForPlayer(player);
    }

    public void bankrupt(Player player, Player toWhom) {
        screen.createDialog("AlertAction", player.getName() + " is bankrupt!", new AlertAction() {
            @Override
            public void apply() {
                nextPlayer();
            }
        });
        // 500 bonus
        if (toWhom != null)
            toWhom.receive(500);

        player.isBankrupt = true;
        for (var b : blocks) {
            if (b instanceof Property && ((Property) b).owner == player) {
                ((Property) b).vacate();
            }
        }
        players.remove(player);
        // the game is over
        if (players.size() == 1) {
            screen.gameOver(players.get(0));
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
        try {
            return players.get(turn);
        }
        catch (Exception e) {
            return null;
        }
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

    private int diceRollSum(List<Integer> rolls) {
        lastDiceRoll = rolls.stream().mapToInt(Integer::intValue).sum();
        return lastDiceRoll;
    }

    /**
     * Serves as a wrapper around the
     * {@link GameScreen#createDialog(String, Object...)} method.
     */
    public void createDialog(String type, Object... args) {
        screen.createDialog(type, args);
    }
}