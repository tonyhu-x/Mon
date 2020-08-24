package com.taj.mon;

import java.util.ArrayList;

import com.taj.mon.block.*;

public class Player {
 
    String name = "";

    /**
     * Player number. Should correspond to position in {@link GameInstance#players}.
     */
    int number;
    
    int lastDiceRoll;
    public boolean isBankrupt;

    /**
     * The amount of cash the player has.
     */
    public int cashAmt;

    /**
     * The money the player keeps in the bank.
     */
    public int savings;
    private ArrayList<Property> properties = new ArrayList<>();

    /**
     * The position of the player on the map. Because the player may be moving
     * forward or backward, the max position is greater than the number of blocks on
     * the map. The starting position is 0.
     */
    private int position;

    /**
     * The number of rounds the player has completed, equivalent to the number of
     * times he/she has passed Go.
     */
    private int roundCount;
    
    /**
     * The group number of the block which the player is currently on.
     */
    private int group;
    public int immobilized;

    /**
     * The player becomes immobilized if he/she is in jail/the hospital.
     */
    private GameInstance instance;

    public Player(GameInstance instance, String name, int number, int cashAmt) {
        this.instance = instance;
        this.name = name;
        this.number = number;
        this.cashAmt = cashAmt;
    }

    public void move(int steps) {
        lastDiceRoll = steps;
        if (immobilized > 0) {
            immobilized--;
        }
        if (immobilized > 0) {
            return;
        }
        forward(steps);
        System.out.println("The current position is " + position);
    }

    public boolean isForward() {
        if (position < 50) return true;
        return false;
    }

    public void forward(int steps) {
        // the player has passed Go
        if (position + steps >= GameInstance.MAP_SIZE) {
            roundCount++;
            receive(Go.salary(roundCount));
            instance.createDialog("ShowAlert", "You have received $" + Go.salary(roundCount) + ".");
        }
        // the player has passed a bank
        if (position < 13 && position + steps >= 13
            || position < 27 && position + steps >= 27
            || position < 53 && position + steps >= 53
            || position < 67 && position + steps >= 67)
        {
            if (Bank.processTransactions(this)) {
                instance.createDialog("ShowAlert", "Transactions have been processed.");
            }
        }
        position = (position + steps) % GameInstance.MAP_SIZE;
        setGroup();
    }

    public void backward(int steps) {
        position = position - steps;
        if (position < 0) {
            position += GameInstance.MAP_SIZE;
        }
        setGroup();
    }

    // directly teleports player, does not pass Go or bank
    public void to(int pos) {
        position = pos;
        System.out.println(pos);
        setGroup();
    }

    public void getProperty(Property property) {
        getProperty(property, property.getPurchasePrice());
    }
    
    public void getProperty(Property property, int price) {
        properties.add(property);
        this.payTo(null, price);
        property.owner = this;
    }

    public void transferProperty(Property property, Player newOwner) {
        properties.remove(property);
        newOwner.addProperty(property);
    }

    public void sellProperty(Property property) {
        while (downgradeable(property)) {
            property.downgrade();
        }
        properties.remove(property);
        this.receive((int) (0.5 * property.getPurchasePrice()));
        property.owner = null;
    }
    
    private void addProperty(Property property) {
        properties.add(property);
        property.owner = this;
    }

    /**
     * Pays some money.
     * 
     * @param player {@code null} is understood to refer to the bank
     * @param amt the amount paid
     */
    public void payTo(Player player, int amt) {
        int result = this.pay(amt);
        if (result == -1) {
            instance.bankrupt(this, player);
            if (player != null) {
                player.receive(netWorth());
            }
            return;
        }
        if (result == 1) {
            instance.bankruptCheck(this);
        }
        if (player != null)
            player.receive(amt); 
    }

    private int pay(int amt) {
        if (this.cashAmt >= amt) {
            this.cashAmt -= amt;
            return 0;
        }
        else if (this.savings + this.cashAmt >= amt) {
            this.savings -= amt - this.cashAmt;
            this.cashAmt = 0;
            return 0;
        }
        else if (netWorth() >= amt){
            this.cashAmt = cashAmt + savings - amt;
            this.savings = 0;
            return 1;
        }
        else {
            return -1;
        }
    }

    public void receive(int amt) {
        this.cashAmt += amt; 
    }
    
    public void reset(int cashAmt) {
        this.cashAmt = cashAmt;
        this.properties = new ArrayList<>();    
        this.position = 0;
    }

    public String getName() {
        return name;
    }
    
    public int getNumber() {
        return number;
    }

    public int getPosition() {
        return position;
    }

    public boolean upgradeable(Property property) {
        return
            this.group == property.getGroup()
            && this == property.owner
            && this.countProperty(property.getGroup()) > property.getLevel();
    }

    public boolean downgradeable(Property property) {
        return
            this == property.owner
            && property.getLevel() > 0;
    }

    /**
     * Counts the number of properties the player has in a group.
     * 
     * @param group the group to count
     * @return {@code 4} if the player has a monopoly, otherwise the exact number
     *         of properties is returned
     */
    private int countProperty(int group) {
        if (instance.isMonopoly(this, group)) {
            return 4;
        }

        int temp = 0;
        for (var p : properties) {
            if (p.getGroup() == group)
                temp++;
        }
        return temp;
    }

    public int netWorth() {
        int worth = savings + cashAmt;
        for (var p : properties) {
            worth += p.worth();
        }
        return worth;
    }

    private void setGroup() {
        group = instance.getBlockGroup(instance.convertPos(position));
    }

}