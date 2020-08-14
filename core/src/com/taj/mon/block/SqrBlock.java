package com.taj.mon.block;

import com.badlogic.gdx.math.Vector2;

public abstract class SqrBlock extends Block {

    public static final float HEIGHT = 35;
    public static final float WIDTH = 35;

    public SqrBlock(String name, int index) {
        super(name, index);
    }

    public Vector2 getDimensions() {
        return new Vector2(WIDTH, HEIGHT);
    }
}