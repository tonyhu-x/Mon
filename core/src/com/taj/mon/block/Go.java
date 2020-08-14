package com.taj.mon.block;

import com.taj.mon.Player;
import com.taj.mon.GameInstance.Task;

public class Go extends SqrBlock {

    public static final int SALARY_MIN = 200;
    public static final int STEP = 50;
    public static final int SALARY_MAX = 600;

    public static int salary(int roundCount) {
        return Math.min(SALARY_MIN + (roundCount - 1) * STEP, SALARY_MAX);
    }

    public Go(String name, int index) {
        super(name, index);
    }

    @Override
    public Task interact(Player player) {
        return Task.NO_OP;
    }

    @Override
    public String getTextureName() {
        return "go";
    }
}