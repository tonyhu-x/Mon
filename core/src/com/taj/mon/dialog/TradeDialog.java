package com.taj.mon.dialog;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.taj.mon.GameScreen;
import com.taj.mon.Player;
import com.taj.mon.block.Property;

public class TradeDialog extends Dialog {

    private GameScreen screen;
    private Player p1, p2;
    private ArrayList<Property> pro1, pro2;
    private Label l1;
    private Label l2;
    private TextButton mode;
    private boolean receive;
    private TextField amountField;

    /**
     * Constructs a trade dialog.
     * 
     * @param title
     * @param skin
     * @param screen
     * @param p1 the property belonging to the player initiating the trade
     * @param p2 the property belonging to the target player
     */

    public TradeDialog(String title, Skin skin, GameScreen screen, Player p1, Player p2,
                                                        ArrayList<Property> pro1, ArrayList<Property> pro2)
    {
        super(title, skin);
        this.screen = screen;
        var table = this.getContentTable();
        table.defaults().width(300).center();
        l1 = new Label("You:", skin);
        l2 = new Label(p2.getName() + ":", skin);
        amountField = new TextField("0", skin);
        this.p1 = p1;
        this.p2 = p2;
        this.pro1 = pro1;
        this.pro2 = pro2;
        table.add(l1);
        table.add(l2);
        table.row();

        if (pro1.isEmpty()) {
            table.add(new Label("None", skin));
        }
        else {
            StringBuilder builder = new StringBuilder();
            for (var p : pro1) {
                builder.append(p.getName() + "\n");
            }
            builder.deleteCharAt(builder.toString().length() - 1);
            table.add(new Label(builder.toString(), skin));
        }
        if (pro2.isEmpty()) {
            table.add(new Label("None", skin));
        }
        else {
            StringBuilder builder = new StringBuilder();
            for (var p : pro2) {
                builder.append(p.getName() + "\n");
            }
            builder.deleteCharAt(builder.toString().length() - 1);
            table.add(new Label(builder.toString(), skin));
        }
        table.row();
        mode = new TextButton("You pay:", skin);
        mode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receive = !receive;
                if (receive) {
                    mode.setText("You get:");
                }
                else {
                    mode.setText("You pay:");
                }
            }
        });
        table.add(mode);
        table.add(amountField);
        
        this.button("Offer", null);
        this.button("Cancel", false);
    }

    @Override
    protected void result(Object object) {
        if (object == null) {
            cancel();
            int amount;
            try {
                amount = Integer.parseInt(amountField.getText());
                if (amount < 0 || receive && amount > p2.cashAmt || !receive && amount > p1.cashAmt) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                amountField.setColor(1, 0, 0, 1);
                cancel();
                return;
            }
            
            this.getButtonTable().clearChildren();
            this.button("Accept", true);
            this.button("Reject", false);
            this.mode.setText(receive ? "You pay:" : "You get:");
            this.mode.setDisabled(true);
            l1.setText(p1.getName() + ":");
            l2.setText("You:");
            amountField.setDisabled(true);
        }
        else if ((boolean) object) {
            int amount = Integer.parseInt(amountField.getText());
            screen.getInstance().trade(p1, p2, pro1, pro2, receive ? -amount : amount);
            screen.exitTrading();
        }
        else {
            screen.exitTrading();
        }
    }
}