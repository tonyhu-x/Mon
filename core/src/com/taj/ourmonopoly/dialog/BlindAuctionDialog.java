package com.taj.ourmonopoly.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.taj.ourmonopoly.GameInstance;
import com.taj.ourmonopoly.GameScreen;
import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.block.Property;

public class BlindAuctionDialog extends Dialog {

    private TextField field;
    private TextButton button;
    private Player highestBidder;
    private Property property;
    private GameInstance instance;
    private GameScreen screen;
    private int highestBid = -1;
    private int ind;
	private Label label;

    public BlindAuctionDialog(String title, Skin skin, GameScreen screen, Property property) {
        super(title, skin);
        // bidding starts from the current player so they have an advantage
        this.instance = screen.getInstance();
        this.screen = screen;
        this.ind = this.instance.getCurrentPlayer().getNumber();
        this.property = property;
        this.field = new TextField("", skin);
        this.button = new TextButton("Next", skin);
        this.label = new Label(instance.players.get(ind).getName(), skin);
        this.getContentTable().add(label).space(30, 30, 30, 30);
        this.getContentTable().add(field).space(30, 30, 30, 30);
        this.button(button, true);
    }
    
    @Override
    protected void result(Object object) {
        int bid;
        try {
            bid = Integer.parseInt(field.getText());
            if (bid < 0) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            field.setColor(1, 0, 0, 1);
            cancel();
            return;
        }

        field.setColor(1, 1, 1, 1);
        var curPlayer = instance.players.get(ind >= instance.players.size() ? ind - instance.players.size() : ind);
        if (bid > highestBid) {
            highestBidder = curPlayer;
            highestBid = bid;
        }

        // if it's the second last player's turn to bid
        if (ind == instance.getCurrentPlayer().getNumber() + instance.players.size() - 2) {
            button.setText("Finish");
        }
        else if (ind == instance.getCurrentPlayer().getNumber() + instance.players.size() - 1) {
            highestBidder.purchaseProperty(property, highestBid);
            screen.createDialog("ShowAlert", highestBidder.getName() + " acquired the property!");
            screen.updateImages();
            screen.updateLabels();
            return;
        }

        ind++;
        curPlayer = instance.players.get(ind >= instance.players.size() ? ind - instance.players.size() : ind);
        label.setText(curPlayer.getName());
        field.setText("");
        cancel();
    }
}