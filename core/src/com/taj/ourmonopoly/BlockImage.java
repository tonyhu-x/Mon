package com.taj.ourmonopoly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.taj.ourmonopoly.block.Block;

public class BlockImage extends Image {

    private Texture texture;
    private Block block;

    public BlockImage(Block block, float posX, float posY) {
        super(new Texture(block.getImagePath()));
        this.block = block;
        this.setBounds(posX, posY, block.getDimensions().x, block.getDimensions().y);
    }    

}