package com.taj.ourmonopoly.block;

import com.taj.ourmonopoly.GameApp;
import com.taj.ourmonopoly.Player;

public class Bank extends SqrBlock {

    public static final String IMAGE_PATH = GameApp.PATH_TO_ASSETS + "blocks/bank.png";

    public Bank(String name, int index) {
        super(name, index);
    }

    @Override
    public void interact(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getImagePath() {
        return IMAGE_PATH;
    }
}