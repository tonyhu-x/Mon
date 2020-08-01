package com.taj.ourmonopoly.block;

import java.util.Random;

import com.taj.ourmonopoly.GameInstance.Task;
import com.taj.ourmonopoly.Player;

public class Chance extends RectBlock {

    // here is a list of cards you can draw from chance
    // 0: split up the cash
    // 1: go to the hospital
    // 2: go back two spaces

    private static final int CARD_MAX = 3;
    private static final Random random = new Random();
    private static int lastDraw = -1;

    public Chance(String name, int index) {
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
                return Task.SPLIT_CASH;
            case 1:
                return Task.GO_TO_HOSPITAL;
            case 2:
                return Task.BACK_TWO;
            default:
                return Task.NO_OP;
        }
    }
    
    @Override
    public String getTextureName() {
        return "chance";
    }
}