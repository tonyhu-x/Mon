package com.taj.ourmonopoly.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.taj.ourmonopoly.GameInstance;
import com.taj.ourmonopoly.GameScreen;
import com.taj.ourmonopoly.Player;

public class HospitalDialog extends Dialog {

    private Player player;
    private GameInstance instance;
    private GameScreen screen;

    public HospitalDialog(String title, Skin skin, Player player, GameInstance instance, GameScreen screen) {
        super(title, skin);
        this.player = player;
        this.instance = instance;
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
        if (((boolean) object) == true) {
            instance.payAndCure(player);
        }
        else {
            instance.tryToReleaseFromHospital(player);
        }
        screen.updateImages();
        screen.updateLabels();
    }
}