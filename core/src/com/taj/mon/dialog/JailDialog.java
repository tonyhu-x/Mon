package com.taj.mon.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.taj.mon.GameScreen;
import com.taj.mon.Player;

public class JailDialog extends Dialog {

    private Player player;
    private GameScreen screen;

    public JailDialog(String title, Skin skin, GameScreen screen, Player player) {
        super(title, skin);
        this.player = player;
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
        if (((boolean) object)) {
            screen.getInstance().payAndRelease(player);
        }
        else {
            screen.getInstance().tryToReleaseFromJail(player);
        }
    }
}