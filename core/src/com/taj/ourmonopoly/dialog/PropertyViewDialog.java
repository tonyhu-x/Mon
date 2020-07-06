package com.taj.ourmonopoly.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.taj.ourmonopoly.GameScreen;
import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.block.Property;

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
        }
        else {
            this.text("UNOWNED");
        }
        this.getContentTable().row();
        this.text("Level: " + property.getLevel());
        this.getContentTable().row();
        this.text("Number of visits: " + property.getNumOfVisits());

        TextButton button = new TextButton("Upgrade", skin);
        if (player.getGroup() != property.getGroup()
            || player != property.owner
            || player.countProperty(property.getGroup()) <= property.getLevel())
        {
            button.setDisabled(true);
            button.setText("Can't Upgrade");
        }
    
        this.button(button, true);
    }
    
    @Override
    protected void result(Object object) {
        if (((boolean) object) == true) {
            this.property.upgrade();
            screen.updateImages();
            screen.updateLabels();
        }
    }
}