package com.taj.ourmonopoly.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.block.Property;

public class PropertyPurchaseDialog extends Dialog {

    Property property;
    Player player;
    TextButton purchaseButton;

    public PropertyPurchaseDialog(String title, Skin skin, Property property, Player player) {
        super(title, skin);
        this.setSize(1000, 600);
        this.setColor(0, 1, 0, 1);
        this.property = property;
        this.player = player;
        this.button("Purchase Property", true);    
    }

    @Override
    protected void result(Object object) {
        if (((boolean) object) == true) {
            player.purchaseProperty(property);            
        }
    }
    
    
}