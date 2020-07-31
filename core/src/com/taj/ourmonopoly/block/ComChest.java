package com.taj.ourmonopoly.block;

import java.util.Random;

import com.taj.ourmonopoly.Player;
import com.taj.ourmonopoly.GameInstance.Task;

public class ComChest extends RectBlock {

    // here is a list of cards you can draw from a community chest
    // 0: pay $100
    // 1: get $50
    // 2: go to jail

    private static final int CARD_MAX = 3;
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
                player.pay(100);
                return Task.PAY_HUNDRED;
            case 1:
                player.receive(50);
                return Task.RECEIVE_FIFTY;
            case 2:
                return Task.GO_TO_JAIL;
            default:
                return Task.NO_OP;

        }
    }

    @Override
    public String getTextureName() {
        return "comChest";
    }
}