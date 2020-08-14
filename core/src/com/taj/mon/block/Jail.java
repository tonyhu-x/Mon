package com.taj.mon.block;

import java.util.ArrayList;

import com.taj.mon.Player;
import com.taj.mon.GameInstance.Task;

public class Jail extends SqrBlock {

    private ArrayList<Player> inmates = new ArrayList<>();

    public Jail(String name, int index) {
        super(name, index);
    }

    @Override
    public Task interact(Player player) {
        if (inmates.contains(player)) {
            return Task.CREATE_JAIL_DIALOG;
        }
        return Task.NO_OP;
    }

    @Override
    public String getTextureName() {
        return "jail";
    }

    public void accept(Player inmate) {
        inmate.to(index);
        inmate.immobilized = 4;
        inmates.add(inmate);
    }

    public void release(Player inmate) {
        inmate.immobilized = 0;
        inmates.remove(inmate);
    }
}