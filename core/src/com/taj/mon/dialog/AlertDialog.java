package com.taj.mon.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AlertDialog extends Dialog {

    public AlertDialog(String title, Skin skin, String message) {
        super(title, skin);
        
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (x < 0 || x > AlertDialog.this.getWidth() || y < 0 || y > AlertDialog.this.getHeight()) {
                    AlertDialog.this.hide();
                    return true;
                }
                return false;
            }
        });

        this.text(message);
    }
}