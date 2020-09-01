package com.taj.mon;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class DiceVisual extends Image {
    
    private static final float ANIM_DURATION = 1.2f;
    private static final float ANIM_SUSPEND = 1.0f;
    private static final float ANIM_REFRESH_FREQ = 0.2f;
    private static final float REFRESH_FREQ = 0.6f;
    private GameScreen screen;
    private float timeE;
    private float subE;
    private Dice dice;
    private Dice internalDice;
    private int target;
    private int whichDice;
    private boolean inAction;

    public DiceVisual(GameScreen screen) {
        super(TextureInventory.getDiceAtlas().getRegions().get(0));
        this.dice = new Dice();
        this.internalDice = new Dice();
        this.screen = screen;
        this.setVisible(false);
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getDiceRoll();
                return true;
            }
        });
    }

    public void show() {
        timeE = 0;
        whichDice = new Random().nextInt(7);
        this.setDrawable(new TextureRegionDrawable(TextureInventory.getDiceRegion(whichDice, internalDice.next())));
        this.setVisible(true);
    }

    public void getDiceRoll() {
        startAction(dice.next());
    }

    private void startAction(int target) {
        timeE = 0;
        subE = 0;
        this.target = target;
        inAction = true;
    }

    @Override
    public void act(float delta) {
        if (!isVisible()) {
            return;
        }
        timeE += delta;
        subE += delta;
        if (!inAction && timeE > REFRESH_FREQ) {
            timeE = 0;
            this.setDrawable(new TextureRegionDrawable(TextureInventory.getDiceRegion(whichDice, internalDice.next())));
        }
        else if (inAction) {
            if (timeE > ANIM_DURATION + ANIM_SUSPEND) {
                this.setVisible(false);
                this.inAction = false;
                screen.receiveDiceRoll(target);
            }
            else if (timeE > ANIM_DURATION) {
                this.setDrawable(new TextureRegionDrawable(TextureInventory.getDiceRegion(whichDice, target)));
            }
            else if (subE > ANIM_REFRESH_FREQ) {
                subE = 0;
                this.setDrawable(new TextureRegionDrawable(TextureInventory.getDiceRegion(whichDice, internalDice.next())));
            }
        }
    }
}