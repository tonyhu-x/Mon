package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.GameApp;
import com.taj.ourmonopoly.GameInstance;
import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.dialog.PropertyPurchaseDialog;

public class Property extends RectBlock {

    private static final String IMAGE_PATH = GameApp.PATH_TO_ASSETS + "blocks/propertyL0.png";
    /**
     * Level of this property. The possible levels include 0-4. Level 0 means that
     * the property is unimploved.
     */
    int level;
    int group;
    int purchasePrice;
    int[] rent;
    Player owner;

    public Property(String name, int index, int group, int purchasePrice, int[] rent) {
        super(name, index);
        this.group = group;
        this.purchasePrice = purchasePrice;
        this.rent = rent;
    }

    @Override
    public int interact(Player player) {
        // the player now needs to purchase this property
        if (owner == null) {
            return GameInstance.TASK_CREATE_PURCHASE_DIALOG;
        }

        return 0;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public String getImagePath() {
        return IMAGE_PATH;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }
}