package com.taj.mon.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.taj.mon.GameScreen;
import com.taj.mon.Player;

public class HospitalDialog extends Dialog {

    private Player player;
    private GameScreen screen;

    public HospitalDialog(String title, Skin skin, GameScreen screen, Player player) {
        super(title, skin);
        this.player = player;
        this.screen = screen;
        this.text(
            "You have "
            + player.immobilized
            + (player.immobilized == 1? " round " : " rounds ")
            + "left in the hospital"
        );
        this.button("Get out ($100)", true);
        this.button("Test your luck", false);
    }
    
    @Override
    protected void result(Object object) {
        if (((boolean) object)) {
            screen.getInstance().payAndCure(player);
        }
        else {
            screen.getInstance().tryToReleaseFromHospital(player);
        }
    }
}