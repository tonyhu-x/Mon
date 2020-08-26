package com.taj.mon;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PlayerImage extends Image {
    
    private class VirtualImage extends Image {

        private static final float EXIST_DURATION = 0.45f;
        private float timeSinceSpawn;

        private VirtualImage(PlayerImage image) {
            super(image.getDrawable());
            this.setSize(WIDTH, HEIGHT);
            this.setPosition(image.getX(), image.getY());
            this.addAction(Actions.fadeOut(EXIST_DURATION));
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            this.toFront();
            super.draw(batch, parentAlpha);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            this.timeSinceSpawn += delta;
            if (this.timeSinceSpawn > EXIST_DURATION) {
                this.remove();
            }
        }

    }

    public static final int WIDTH = 10; 
    public static final int HEIGHT = 10; 

    Player player;
    
    /**
     * The {@link BlockImage} cantaining the player.
     */
    private BlockImage parentBlock;
    private List<BlockImage> route;
    private GameScreen screen;
    private float delta;

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
        super.act(delta);
        this.delta += delta;
        if (route != null && !route.isEmpty() && this.delta > 0.08f) {
            this.setBlockParent(route.remove(0));
            this.delta = 0;
        }
    }

    public void addRoute(List<BlockImage> route) {
        this.route = route;
    }

    public boolean emptyRoute() {
        return route == null || route.isEmpty();
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
        screen.addActorToStage(new VirtualImage(this));
        this.setPosition(x, y);
    }

    public BlockImage getParentBlock() {
        return parentBlock;
    }
}