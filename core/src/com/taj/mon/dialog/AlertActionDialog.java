package com.taj.mon.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.taj.mon.GameScreen;

public class AlertActionDialog extends AlertDialog {

    public static interface AlertAction {

        public void apply();
    }

    public AlertActionDialog(String title, Skin skin, GameScreen screen, String message, AlertAction action) {
        super(title, skin, message);
        this.clearListeners();
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (x < 0 || x > AlertActionDialog.this.getWidth() || y < 0 || y > AlertActionDialog.this.getHeight()) {
                    AlertActionDialog.this.hide();
                    action.apply();
                    return true;
                }
                return false;
            }
        });

    }
}