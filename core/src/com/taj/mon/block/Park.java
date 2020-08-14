package com.taj.mon.block;

import com.taj.mon.Player;
import com.taj.mon.GameInstance.Task;

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