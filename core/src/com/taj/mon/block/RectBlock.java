package com.taj.mon.block;

import com.badlogic.gdx.math.Vector2;

public abstract class RectBlock extends Block {

    public static final float HEIGHT = 35;
    public static final float WIDTH = 25;
    
    public RectBlock(String name, int index) {
        super(name, index);
    }

    public Vector2 getDimensions() {
        return new Vector2(WIDTH, HEIGHT);
    }
}