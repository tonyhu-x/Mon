package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.GameInstance;
import com.taj.ourmonopoly.Player;

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

    @Override
    public int interact(Player player) {
        numOfVisits++;
        // the player now needs to purchase this property
        if (owner == null) {
            return GameInstance.TASK_CREATE_PURCHASE_DIALOG;
        }
        else if (owner == player) {
            return GameInstance.TASK_CREATE_UPGRADE_DIALOG;
        }
        else {
            player.payTo(owner, rent[level]);
            return GameInstance.TASK_CREATE_PAY_RENT_DIALOG;
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
}