package com.taj.mon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PlayerImage extends Image {

    public static final int WIDTH = 10; 
    public static final int HEIGHT = 10; 

    Player player;
    
    /**
     * The {@link BlockImage} cantaining the player.
     */
    private BlockImage parentBlock;
    private GameScreen screen;
    private float deltaX, deltaY;

    private InputListener listener = new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (screen.isTrading()) {
                screen.selectPlayer(player);
            }

            return true;
        }
    };

    public PlayerImage(Player player, BlockImage parent, GameScreen screen) {
        super(TextureInventory.getToken("tokenP" + (player.number + 1)));
        this.player = player;
        this.screen = screen;
        this.setSize(WIDTH, HEIGHT);
        this.setX(-1);
        this.setBlockParent(parent);
        this.addListener(listener);
    }

    /**
     * A stand-alone image.
     * 
     * @param player the player to represent
     */
    public PlayerImage(Player player, GameScreen screen) {
        super(TextureInventory.getToken("tokenP" + (player.number + 1)));
        this.player = player;
        this.screen = screen;
        this.addListener(listener);
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
        if (parentBlock == newParent) {
            return;
        }
        if (parentBlock != null) {
            parentBlock.removeImage(this);
        }
        newParent.pushImage(this);
        parentBlock = newParent;
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