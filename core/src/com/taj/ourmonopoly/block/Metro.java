package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.GameInstance.Task;
import com.taj.ourmonopoly.Player;

public class Metro extends RectBlock {

    private static int posDiff = -1;

    /**
     * {@code true} for the first metro, {@code false} for the second.
     */
    private boolean which;

    public Metro(String name, int index) {
        super(name, index);

        if (posDiff == -1) {
            posDiff = index;
            which = true;
        }
        else {
            posDiff = index - posDiff;
        }
    }

    @Override
    public Task interact(Player player) {
        return Task.METRO;
    }

    public void movePlayer(Player player) {
        if (which) {
            if (player.isForward()) {
                player.forward(posDiff);
            }
            else {
                player.backward(posDiff);
            }
        }
        else {
            if (player.isForward()) {
                player.backward(posDiff);
            }
            else {
                player.forward(posDiff);
            }
        }

    }

    @Override
    public String getTextureName() {
        return "metro";
    }
}