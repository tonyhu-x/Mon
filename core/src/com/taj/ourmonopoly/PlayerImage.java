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

    public PlayerImage(Player player, BlockImage parent) {
        super(TextureInventory.getToken("tokenP" + (player.number + 1)));
        this.player = player;
        this.setSize(WIDTH, HEIGHT);
        this.setBlockParent(parent);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.toFront();
        super.draw(batch, parentAlpha);
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
}