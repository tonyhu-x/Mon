package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.GameApp;
import com.taj.ourmonopoly.Player;

public class Jail extends SqrBlock {

    public Jail(String name, int index) {
        super(name, index);
    }

    @Override
    public int interact(Player player) {
        return 0;
    }

    @Override
    public String getTextureName() {
        return "jail";
    }
}