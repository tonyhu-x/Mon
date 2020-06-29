package com.taj.ourmonopoly;

import java.util.Random;

public class Dice {

    private Random generator = new Random();

    public int next() {
        return generator.nextInt(6) + 1;
    }
    
}