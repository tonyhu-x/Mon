package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.GameApp;
import com.taj.ourmonopoly.Player;

public class Metro extends RectBlock {

    private static final String IMAGE_PATH = GameApp.PATH_TO_ASSETS + "blocks/metro.png";
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
    public int interact(Player player) {
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

        return 0;
    }

    @Override
    public String getImagePath() {
        return IMAGE_PATH;
    }
}