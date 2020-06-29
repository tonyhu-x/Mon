package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.Player;

public class Chance extends RectBlock {

    private static int cardIndex;

    public Chance(String name, int index) {
        super(name, index);
    }

    @Override
    public int interact(Player player) {
        return 0;
    }
    
    @Override
    public String getTextureName() {
        return "chance";
    }
}