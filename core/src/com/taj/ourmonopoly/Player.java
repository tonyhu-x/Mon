package com.taj.ourmonopoly;

import java.util.ArrayList;
import com.taj.ourmonopoly.block.*;

public class Player {
 
    String name = "";

    /**
     * Player number. Should correspond to position in {@link GameInstance#players}.
     */
    int number;
    
    int lastDiceRoll;

    /**
     * The amount of cash the player has.
     */
    private int cashAmt;
    private ArrayList<Property> properties = new ArrayList<>();

    /**
     * The position of the player on the map. Because the player may be moving
     * forward or backward, the max position is greater than the number of blocks on
     * the map. The starting position is 0.
     */
    private int position;
    
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

    public void to(int pos) {
        position = pos;
        System.out.println(pos);
        setGroup();
    }

    public void purchaseProperty(Property property) {
        properties.add(property);
        cashAmt -= property.getPurchasePrice();
        property.owner = this;
        //TODO: check if the player can actually afford the property
    }

    public void payTo(Player player, int amt) {
        //TODO: check if bankrupt
        this.pay(amt);
        player.receive(amt);        
    }

    public void pay(int amt) {
        this.cashAmt -= amt;
    }

    public void receive(int amt) {
        this.cashAmt += amt; 
    }
    
    public void reset(int cashAmt) {
        this.cashAmt = cashAmt;
        this.properties = new ArrayList<>();    
        this.position = 0;
    }

    public int getCashAmt() {
        return cashAmt;
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
            && this.countProperty(property.getGroup()) <= property.getLevel();
    }

    /**
     * Count the number of properties the player has in a group.
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

    private void setGroup() {
        group = instance.getBlockGroup(instance.convertPos(position));
    }

    public void setCashAmt(int cashAmt) {
        this.cashAmt = cashAmt;
    }
}