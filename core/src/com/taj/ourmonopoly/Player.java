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
    private ArrayList<Block> properties = new ArrayList<>();
    private int position;

    public Player(String name, int number, int cashAmt) {
        this.name = name;
        this.number = number;
        this.cashAmt = cashAmt;
    }

    public void move(int steps) {
        lastDiceRoll = steps;
        forward(steps);
        Block.queryBlock(this, position);
    }

    public boolean isForward() {
        if (position < 50) return true;
        return false;
    }

    public void forward(int steps) {
        position = (position + steps) % Block.MAP_SIZE;
    }

    public void backward(int steps) {
        position = position - steps;
        if (position < 0) {
            position += 80;
        }
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