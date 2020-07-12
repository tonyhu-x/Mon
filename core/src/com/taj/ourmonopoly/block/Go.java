package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.GameInstance.Task;

public class Go extends SqrBlock {

    /**
     * The amount of money a player gets each time they pass Go.
     */
    private static final int SALARY = 200;

    public Go(String name, int index) {
        super(name, index);
    }

    @Override
    public Task interact(Player player) {
        player.receive(200);
        return Task.NO_OP;
    }

    @Override
    public String getTextureName() {
        return "go";
    }
}