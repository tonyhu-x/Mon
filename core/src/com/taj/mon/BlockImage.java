package com.taj.mon;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.taj.mon.block.Bank;
import com.taj.mon.block.Block;
import com.taj.mon.block.Property;

public class BlockImage extends Image {

    private GameScreen screen;
    private Block block;
    private String prevTextureName;
    private ArrayList<PlayerImage> images;
    private int rotate;
    private boolean selected;

    public BlockImage(Block block, GameScreen screen, float posX, float posY, int rotate) {
        super(TextureInventory.getRegion(block.getTextureName()));
        prevTextureName = block.getTextureName();
        this.screen = screen;
        this.block = block;
        this.rotate = rotate;
        this.images = new ArrayList<>();
        this.setBounds(posX, posY, block.getDimensions().x, block.getDimensions().y);
        this.rotateBy(rotate * 90);
        this.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (screen.isTrading()) {
                    if (selected) {
                        if (deselect()) {
                            screen.deselectImage(BlockImage.this);
                        }
                    }
                    else {
                        if (select()) {
                            screen.selectImage(BlockImage.this);
                        }
                    }
                }
                else if (screen.isSelling()) {
                    // no need to check ownership since other blocks are already disabled
                    Property p = (Property) BlockImage.this.block;
                    p.owner.sellProperty(p);
                }
                else {
                    if (block instanceof Property) {
                        screen.createDialog("ViewProperty", block);
                    }
                    else if (block instanceof Bank) {
                        screen.createDialog("Bank");
                    }
                }
                return true;                
            }

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
        if (block.getTextureName() != prevTextureName) {
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

    public boolean select() {
        // you can only select unimproved property for trading purporses
        if (block instanceof Property && ((Property) block).owner != null && ((Property) block).getLevel() == 0) {
            selected = true;
            this.setColor(1, 1, 1, 0.6f);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deselect() {
        if (block instanceof Property && ((Property) block).owner != null && ((Property) block).getLevel() == 0) {
            selected = false;
            this.setColor(1, 1, 1, 1);
            return true;
        }
        else {
            return false;
        }
    }
}