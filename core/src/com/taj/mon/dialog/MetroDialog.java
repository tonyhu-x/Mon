package com.taj.mon.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.taj.mon.GameScreen;
import com.taj.mon.Player;
import com.taj.mon.block.Metro;

public class MetroDialog extends Dialog {

    private Metro metro;
    private Player player;
    private GameScreen screen;

    public MetroDialog(String title, Skin skin, GameScreen screen, Metro metro, Player player) {
        super(title, skin);
        this.metro = metro;
        this.player = player;
        this.screen = screen;
        this.text("Do you want to use the metro?");
        this.button("Yes", true);
        this.button("No", false);
    }

    @Override
    protected void result(Object object) {
        if (((boolean) object) == true) {
            metro.movePlayer(player);
        }
        else {
            this.hide();
        }
    }
    
}