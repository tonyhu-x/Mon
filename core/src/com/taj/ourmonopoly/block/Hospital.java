package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.Player;

public class Hospital extends SqrBlock {

    public Hospital(String name, int index) {
        super(name, index);
    }

    @Override
    public int interact(Player player) {
        return 0;
    }

    @Override
    public String getTextureName() {
        return "hospital";
    }
}