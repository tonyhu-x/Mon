package com.taj.ourmonopoly.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.taj.ourmonopoly.block.Property;

public class PropertyViewDialog extends Dialog {

    public PropertyViewDialog(String title, Skin skin, Property property) {
        super(title, skin);
        this.setModal(true);
        // hide the diolog when the user clicks outside
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (x < 0 || x > PropertyViewDialog.this.getWidth() || y < 0 || y > PropertyViewDialog.this.getHeight()) {
                    PropertyViewDialog.this.hide();
                    return true;
                }
                return false;
            }
        });

        this.getContentTable().row();
        this.text(property.getName());
        this.getContentTable().row();
        if (property.owner != null) {
            this.text(property.owner.getName());
        }
        else {
            this.text("UNOWNED");
        }
        this.getContentTable().row();
        this.text("Level: " + property.getLevel());
        this.getContentTable().row();
        this.text("Number of visits: " + property.getNumOfVisits());
    }
    
}