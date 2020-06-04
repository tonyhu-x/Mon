package com.taj.ourmonopoly.block;

import com.badlogic.gdx.math.Vector2;
import com.taj.ourmonopoly.GameApp;
import com.taj.ourmonopoly.Player;

public class Go extends SqrBlock {

    public static final String IMAGE_PATH = GameApp.PATH_TO_ASSETS + "blocks/go.png";

    public Go(String name, int index) {
        super(name, index);
    }

    @Override
    public void interact(Player player) {

    }

    @Override
    public String getImagePath() {
        return IMAGE_PATH;
    }
}