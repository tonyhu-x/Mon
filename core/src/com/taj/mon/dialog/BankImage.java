package com.taj.mon.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.taj.mon.BlockImage;
import com.taj.mon.GameScreen;
import com.taj.mon.block.Block;

public class BankImage extends BlockImage {

    public BankImage(Block block, GameScreen screen, float posX, float posY, int rotate) {
        super(block, screen, posX, posY, rotate);
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                screen.createDialog("Bank");
                return true;
            }
        });
    }
    
}