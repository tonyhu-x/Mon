package com.taj.mon;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.taj.mon.block.Block;

public class BlockImage extends Image {

    public enum State {
        NORMAL, SELL, TRADE
    }

    protected GameScreen screen;
    protected Block block;
    private String prevTextureName;
    private ArrayList<PlayerImage> images;
    private int rotate;
    protected boolean selected;

    public BlockImage(Block block, GameScreen screen, float posX, float posY, int rotate) {
        super(TextureInventory.getRegion(block.getTextureName()));
        prevTextureName = block.getTextureName();
        this.screen = screen;
        this.block = block;
        this.rotate = rotate;
        this.images = new ArrayList<>();
        this.setBounds(posX, posY, block.getDimensions().x, block.getDimensions().y);
        this.rotateBy(rotate * 90f);
        this.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                BlockImage.this.toFront();
                BlockImage.this.addAction(
                    Actions.parallel(
                        Actions.scaleTo(
                            1.2f,
                            1.2f,
                            0.3f
                        ),
                        Actions.moveBy(
                            -0.1f * block.getDimensions().x,
                            0,
                            0.3f
                        )
                    )
                );
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                BlockImage.this.addAction(
                    Actions.parallel(
                        Actions.scaleTo(
                            1,
                            1,
                            0.3f
                        ),
                        Actions.moveBy(
                            0.1f * block.getDimensions().x,
                            0,
                            0.3f
                        )
                    )
                );
            }
        });
    }

    @Override
    public void act(float delta) {
        if (!block.getTextureName().equals(prevTextureName)) {
            this.setDrawable(new TextureRegionDrawable(TextureInventory.getRegion(block.getTextureName())));
            this.prevTextureName = block.getTextureName();
        }
    }

    public void pushImage(PlayerImage image) {
        images.add(image);
        if (this.rotate == 0) {
            image.newTarget(this.getX() + (images.size() - 1) * PlayerImage.WIDTH, this.getY());
        }
        else if (this.rotate == -1) {
            image.newTarget(this.getX(), this.getY() + images.size() * rotate * PlayerImage.HEIGHT);
        }
        // otherwise rotate == 1
        else {
            image.newTarget(this.getX() - PlayerImage.WIDTH, this.getY() + (images.size() - 1) * rotate * PlayerImage.HEIGHT);
        }
    }

    public void removeImage(PlayerImage image) {
        images.remove(image);
    }

    public void stateChanged(State newState) {
        switch (newState) {
            case NORMAL:
                enable();
                break;
            case TRADE:
            case SELL:
                disable();
                break;
            default:
                break;
        }
    }

    public void targetPlayerChanged(Player newPlayer) {
        // implemented in subclasses
    }

    public void disable() {
        if (selected)
            return;
        this.setColor(0.4f, 0f, 0.5f, 1f);
        this.setTouchable(Touchable.disabled);
    }

    public void enable() {
        if (selected)
            return;
        this.setColor(1, 1, 1, 1);
        this.setTouchable(Touchable.enabled);
    }

    public Block getBlock() {
        return block;
    }

    // only property can be selected
    public boolean select() {
        return false;
    }

    public boolean deselect() {
        return false;
    }
}