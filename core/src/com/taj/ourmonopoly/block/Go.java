package com.taj.ourmonopoly.block;

import com.badlogic.gdx.math.Vector2;
import com.taj.ourmonopoly.GameApp;
import com.taj.ourmonopoly.Player;

public class Go extends SqrBlock {

    public static final String IMAGE_PATH = GameApp.PATH_TO_ASSETS + "blocks/go.png";

    /**
     * The amount of money a player gets each time they pass Go.
     */
    private static final int SALARY = 200;

    public Go(String name, int index) {
        super(name, index);
    }

    @Override
    public int interact(Player player) {
        player.setCashAmt(player.getCashAmt() + SALARY);
        return 0;
    }

    @Override
    public String getImagePath() {
        return IMAGE_PATH;
    }
}