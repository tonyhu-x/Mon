package com.taj.ourmonopoly;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.taj.ourmonopoly.block.Block;
import com.taj.ourmonopoly.block.Property;

public class BlockImage extends Image {

    private Block block;
    private String prevTextureName;
    private GameScreen screen;


    public BlockImage(Block block, GameScreen screen, float posX, float posY, int rotate) {
        super(TextureInventory.getRegion(block.getTextureName()));
        prevTextureName = block.getTextureName();
        this.block = block;
        this.screen = screen;
        this.setBounds(posX, posY, block.getDimensions().x, block.getDimensions().y);
        this.rotateBy(rotate * 90);
        this.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (block instanceof Property) {
                    screen.createDialog("ViewProperty", block);
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
                    Actions.parallel (
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

    public void updateImage() {
        if (block.getTextureName() != prevTextureName) {
            this.setDrawable(new TextureRegionDrawable(TextureInventory.getRegion(block.getTextureName())));
            this.prevTextureName = block.getTextureName();
        }
    }
}