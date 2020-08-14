package com.taj.mon.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.taj.mon.Player;
import com.taj.mon.block.Bank;

public class BankDialog extends Dialog {

    private TextField tf;
    private Player player;

    public BankDialog(String title, Skin skin, Player player) {
        super(title, skin);
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (x < 0 || x > BankDialog.this.getWidth() || y < 0 || y > BankDialog.this.getHeight()) {
                    BankDialog.this.hide();
                    return true;
                }
                return false;
            }
        });
        this.player = player;
        this.text("You have $" + player.savings + " in the bank.");
        this.getContentTable().row();
        this.text("Transactions will only be processed when you pass either bank.");

        for (var t : Bank.getTransactions(player)) {
            this.getContentTable().row();
            this.text(t.getType() + ": $" + t.getAmount());
        }

        tf = new TextField("0", skin);
        tf.setAlignment(Align.center);
        this.getContentTable().row();
        this.getContentTable().add(tf);
        this.button("Deposit", true);
        this.button("Withdraw", false);
    }

    @Override
    protected void result(Object object) {
        var str = tf.getText();
        int amt = 0;
        try {
            amt = Integer.parseInt(str);
            if (amt <= 0) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            tf.setColor(1, 0, 0, 1);
            cancel();
            return;
        }

        if (((boolean) object) == true) {
            Bank.addTransaction(new Bank.Transaction(player, amt));
        }
        else {
            Bank.addTransaction(new Bank.Transaction(player, -amt));
        }
    }
    
}