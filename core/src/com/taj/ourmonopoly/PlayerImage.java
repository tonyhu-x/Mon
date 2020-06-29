package com.taj.ourmonopoly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PlayerImage extends Image {

    public static final int WIDTH = 15; 
    public static final int HEIGHT = 15; 

    private GameScreen screen;
    Player player;
    
    /**
     * The {@link BlockImage} cantaining the player.
     */
    private BlockImage parent;

    public PlayerImage(GameScreen screen, Player player, BlockImage parent) {
        super(new Texture(GameApp.PATH_TO_ASSETS + "token.png"));
        this.screen = screen;
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