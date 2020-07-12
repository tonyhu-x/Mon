package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.GameInstance.Task;

public class Property extends RectBlock {

    /**
     * Level of this property. The possible levels include 0-4. Level 0 means that
     * the property is unimploved.
     */
    int level;
    int group;
    int purchasePrice;
    int[] rent;
    // the number of times that the block has been visited
    int numOfVisits;
    public Player owner;

    public Property(String name, int index, int group, int purchasePrice, int[] rent) {
        super(name, index);
        this.group = group;
        this.purchasePrice = purchasePrice;
        this.rent = rent;
    }

    public void upgrade() {
        if (this.level == 4) {
            throw new RuntimeException("That's weird. Check your code logic.");
        }
        
        level++;
        owner.pay(purchasePrice * (int) Math.pow(2, level));
    }

    @Override
    public Task interact(Player player) {
        numOfVisits++;
        // the player now needs to purchase this property
        if (owner == null) {
            return Task.CREATE_PURCHASE_DIALOG;
        }
        else if (owner == player) {
            return Task.NO_OP;
        }
        else {
            // player.payTo(owner, rent[level]);
            return Task.PAY_RENT;
        }
    }

    @Override
    public String getTextureName() {
        if (owner == null) {
            return "propertyUnowned";
        }
        else {
            return "propertyL" + level + "P" + (owner.getNumber() + 1); 
        }
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public int getLevel() {
        return level;
    }

    public int getNumOfVisits() {
        return numOfVisits;
    }

    public int getCurrentRent() {
        return rent[level];
    }

    public int getGroup() {
        return group;
    }
}