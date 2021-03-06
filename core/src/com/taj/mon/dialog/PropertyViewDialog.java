package com.taj.mon.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.taj.mon.GameScreen;
import com.taj.mon.Player;
import com.taj.mon.block.Property;

public class PropertyViewDialog extends Dialog {

    GameScreen screen;
    Property property;

    public PropertyViewDialog(String title, Skin skin, GameScreen screen, Property property, Player player) {
        super(title, skin);
        this.property = property;
        this.screen = screen;
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
            this.getContentTable().row();
            this.text("Level: " + property.getLevel());
            this.getContentTable().row();
            this.text("Current rent: $" + property.getCurrentRent());
        }
        else {
            this.text("UNOWNED");        
        }
        this.getContentTable().row();
        this.text("Number of visits: " + property.getNumOfVisits());

        TextButton button = new TextButton("Upgrade", skin);
        if (!player.upgradeable(property)) {
            button.setDisabled(true);
            button.setText("Can't Upgrade");
        }
        TextButton button2 = new TextButton("Downgrade", skin);
        if (!player.downgradeable(property)) {
            button2.setDisabled(true);
            button2.setText("Can't Downgrade");
        }
    
        this.button(button, true);
        this.button(button2, false);
    }
    
    @Override
    protected void result(Object object) {
        if (((boolean) object) == true) {
            this.property.upgrade();
        }
        else {
            this.property.downgrade();
        }
    }
}