package com.taj.ourmonopoly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.taj.ourmonopoly.block.Block;

public class BlockActor extends Actor {

    private Texture texture;
    private Block block;
    private float posX, posY;

    public BlockActor(Block block, float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        setBounds(posX, posY, block.getDimensions().x , block.getDimensions().y);
    }    

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, posX, posY);
    }
}