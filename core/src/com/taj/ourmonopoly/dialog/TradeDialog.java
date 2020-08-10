package com.taj.ourmonopoly.dialog;

import java.text.NumberFormat;
import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.taj.ourmonopoly.GameScreen;
import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.block.Property;

public class TradeDialog extends Dialog {

    private GameScreen screen;
    private Player p1, p2;
    private ArrayList<Property> pro1, pro2;
    private Label l1;
    private Label l2;
    private TextField tf1, tf2;

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
        tf1 = new TextField("0", skin);
        tf2 = new TextField("0", skin);
        this.p1 = p1;
        this.p2 = p2;
        this.pro1 = pro1;
        this.pro2 = pro2;
        table.add(l1);
        table.add(l2);
        table.row();

        if (pro1.size() == 0) {
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
        if (pro2.size() == 0) {
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
        table.add(tf1);
        table.add(tf2);
        
        this.button("Offer", null);
        this.button("Cancel", false);
    }

    @Override
    protected void result(Object object) {
        if (object == null) {
            cancel();
            int amt1, amt2;
            try {
                amt1 = Integer.parseInt(tf1.getText());
                amt2 = Integer.parseInt(tf2.getText());
                if (amt1 < 0 || amt2 < 0 || amt1 > p1.cashAmt || amt2 > p2.cashAmt) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                tf1.setColor(1, 0, 0, 1);
                tf2.setColor(1, 0, 0, 1);
                cancel();
                return;
            }
            
            this.getButtonTable().clearChildren();
            this.button("Accept", true);
            this.button("Reject", false);
            l1.setText(p1.getName() + ":");
            l2.setText("You:");
            tf1.setDisabled(true);
            tf2.setDisabled(true);
        }
        else if (((boolean) object) == true) {
            int amt1, amt2;
            amt1 = Integer.parseInt(tf1.getText());
            amt2 = Integer.parseInt(tf2.getText());

            screen.getInstance().trade(p1, p2, pro1, pro2, amt1, amt2);
            screen.exitTrading();
        }
        else {
            screen.exitTrading();
        }

        screen.updateLabels();
    }
}