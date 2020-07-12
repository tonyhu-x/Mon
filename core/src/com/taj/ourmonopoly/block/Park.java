package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.GameInstance.Task;
import com.taj.ourmonopoly.Player;

public class Park extends SqrBlock {

    public Park(String name, int index) {
        super(name, index);
    }

    @Override
    public Task interact(Player player) {
        return Task.NO_OP;
    }

    @Override
    public String getTextureName() {
        return "park";
    }
}