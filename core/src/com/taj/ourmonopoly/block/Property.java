package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.GameApp;
import com.taj.ourmonopoly.Player;

public class Property extends RectBlock {

    private static final String IMAGE_PATH = GameApp.PATH_TO_ASSETS + "blocks/propertyL0.png";
    /**
     * Level of this property. Level 0 means unimploved.
     */
    int level;
    int group;

    public Property(String name, int index, int group) {
        super(name, index);
        this.group = group;
    }

    @Override
    public void interact(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getImagePath() {
        return IMAGE_PATH;
    }
}