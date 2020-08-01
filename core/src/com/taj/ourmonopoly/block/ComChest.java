package com.taj.ourmonopoly.block;

import java.util.Random;

import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.GameInstance.Task;

public class ComChest extends RectBlock {

    // here is a list of cards you can draw from a community chest
    // 0: pay $100
    // 1: get $50
    // 2: go to jail
    // 3: advance to Go

    private static final int CARD_MAX = 4;
    private static final Random random = new Random();
    private static int lastDraw = -1;

    public ComChest(String name, int index) {
        super(name, index);
    }

    @Override
    public Task interact(Player player) {
        int cardIndex = random.nextInt(CARD_MAX);
        while (cardIndex == lastDraw) {
            cardIndex = random.nextInt(CARD_MAX);
        }
        lastDraw = cardIndex;

        switch (cardIndex) {
            case 0:
                return Task.PAY_HUNDRED;
            case 1:
                return Task.RECEIVE_FIFTY;
            case 2:
                return Task.GO_TO_JAIL;
            case 3:
                return Task.ADVANCE_TO_GO;
            default:
                return Task.NO_OP;

        }
    }

    @Override
    public String getTextureName() {
        return "comChest";
    }
}