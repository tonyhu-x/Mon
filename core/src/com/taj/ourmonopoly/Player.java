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
    private int position;

    /**
     * The position of the player on the map. The starting position is 0.
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
        forward(steps);
        instance.queryBlock(this, position);
        System.out.println("the current position of Player " + number + " is " + position);
    }

    public boolean isForward() {
        if (position < 50) return true;
        return false;
    }

    public void forward(int steps) {
        position = (position + steps) % GameInstance.MAP_SIZE;
    }

    public void backward(int steps) {
        position = position - steps;
        if (position < 0) {
            position += 80;
        }
    }

    public void purchaseProperty(Property property) {
        properties.add(property);
        cashAmt -= property.getPurchasePrice();
        property.setOwner(this);
        //TODO: check if the player can actually afford the property
    }

    public void reset(int cashAmt) {
        this.cashAmt = cashAmt;
        this.properties = new ArrayList<>();    
        this.position = 0;
    }

    public void setCashAmt(int cashAmt) {
        this.cashAmt = cashAmt;
    }

    public int getCashAmt() {
        return cashAmt;
    }
}