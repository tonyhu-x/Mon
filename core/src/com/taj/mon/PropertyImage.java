package com.taj.mon;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.taj.mon.block.Block;
import com.taj.mon.block.Property;

public class PropertyImage extends BlockImage {

    private State state;

    public PropertyImage(Block block, GameScreen screen, float posX, float posY, int rotate) {
        super(block, screen, posX, posY, rotate);
        this.state = State.NORMAL;
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switch (state) {
                    case NORMAL:
                        screen.createDialog("ViewProperty", block);
                        break;
                    case TRADE:
                        trade();
                        break;
                    case SELL:
                        sell();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void trade() {
        if (deselect()) {
            screen.deselectImage(PropertyImage.this);
        }
        else if (select()) {
            screen.selectImage(PropertyImage.this);
        }
    }

    private void sell() {
        Property p = (Property) block;
        p.owner.sellProperty(p);
        this.disable();
    }

    @Override
    public boolean select() {
        Property property = (Property) this.block;
        if (property.owner != null && property.getLevel() == 0) {
            this.selected = true;
            this.setColor(1, 1, 1, 0.6f);
            return true;
        }
        return false;
    }

    @Override
    public boolean deselect() {
        if (this.selected) {
            selected = false;
            this.setColor(1, 1, 1, 1);
            return true;
        }
        return false;
    }

    @Override
    public void stateChanged(State newState) {
        Property p = (Property) block;
        if (newState == State.NORMAL) {
            deselect();
            enable();
        }
        else if (newState == State.TRADE && (p.owner == null || p.getLevel() != 0)
                    || newState == State.SELL && p.owner != screen.getCurrentPlayer())
        {
            disable();
        }
        state = newState;
    }

    @Override
    public void targetPlayerChanged(Player newPlayer) {
        Property p = (Property) block;
        if (state == State.TRADE) {
            if (p.getLevel() != 0 || p.owner == screen.getCurrentPlayer() || p.owner == null) {
                return;
            }
            if (newPlayer == null) {
                enable();
            }
            else if (p.owner != newPlayer) {
                deselect();
                disable();
            }
            else {
                enable();
            }
        }
        else if (state == State.SELL && p.owner != newPlayer) {
            disable();
        }
    }
}