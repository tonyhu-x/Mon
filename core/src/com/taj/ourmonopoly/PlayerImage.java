package com.taj.ourmonopoly;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PlayerImage extends Image {

    public static final int WIDTH = 10; 
    public static final int HEIGHT = 10; 

    Player player;
    
    /**
     * The {@link BlockImage} cantaining the player.
     */
    private BlockImage parent;
    private float deltaX, deltaY;

    public PlayerImage(Player player, BlockImage parent) {
        super(TextureInventory.getToken("tokenP" + (player.number + 1)));
        this.player = player;
        this.setSize(WIDTH, HEIGHT);
        this.setX(-1);
        this.setBlockParent(parent);
    }

    /**
     * A stand-alone image.
     * 
     * @param player the player to represent
     */
    public PlayerImage(Player player) {
        super(TextureInventory.getToken("tokenP" + (player.number + 1)));
        this.player = player;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.toFront();
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        this.setX(this.getX() + deltaX * 0.1f);
        this.setY(this.getY() + deltaY * 0.1f);
        deltaX *= 0.9;
        deltaY *= 0.9;
    }

    public void setBlockParent(BlockImage newParent) {
        if (parent == newParent) {
            return;
        }
        if (parent != null) {
            parent.removeImage(this);
        }
        newParent.pushImage(this);
        parent = newParent;
    }

    public void newTarget(float x, float y) {
        if (this.getX() == -1) {
            this.setX(x);
            this.setY(y);
            return;
        }
        deltaX = x - this.getX();
        deltaY = y - this.getY();
    }
}