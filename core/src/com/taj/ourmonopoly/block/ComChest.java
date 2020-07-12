package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.GameInstance.Task;

public class ComChest extends RectBlock {

    private static int cardIndex;

    public ComChest(String name, int index) {
        super(name, index);
    }

    @Override
    public Task interact(Player player) {
        return Task.NO_OP;
    }

    @Override
    public String getTextureName() {
        return "comChest";
    }
}