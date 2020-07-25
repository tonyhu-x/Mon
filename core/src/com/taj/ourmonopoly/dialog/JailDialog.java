package com.taj.ourmonopoly.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.taj.ourmonopoly.GameInstance;
import com.taj.ourmonopoly.GameScreen;
import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.block.Jail;

public class JailDialog extends Dialog {

    private Player player;
    private GameInstance instance;
    private GameScreen screen;

    public JailDialog(String title, Skin skin, Player player, GameInstance instance, GameScreen screen) {
        super(title, skin);
        this.player = player;
        this.instance = instance;
        this.screen = screen;
        this.text(
            "You have "
            + player.immobilized
            + (player.immobilized == 1? " round " : " rounds ")
            + "left in jail."
        );
        this.button("Get out ($50)", true);
        this.button("Roll a double", false);
    }

    @Override
    protected void result(Object object) {
        if (((boolean) object) == true) {
            instance.payAndRelease(player);
        }
        else {
            instance.tryToReleaseFromJail(player);
        }
        screen.updateImages();
        screen.updateLabels();
    }
}