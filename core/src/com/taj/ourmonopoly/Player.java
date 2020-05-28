package com.taj.ourmonopoly;

import java.util.ArrayList;

public class Player {
 
    String name = "";
    
    /**
     * The amount of cash the player has.
     */
    private int cashAmt;
    private ArrayList<Property> properties = new ArrayList<>();
    private int position;

    public Player(String name, int cashAmt) {
        this.name = name;
        this.cashAmt = cashAmt;
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