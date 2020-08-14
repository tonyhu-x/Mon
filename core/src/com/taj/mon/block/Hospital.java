package com.taj.mon.block;

import java.util.ArrayList;

import com.taj.mon.Player;
import com.taj.mon.GameInstance.Task;

public class Hospital extends SqrBlock {

    private ArrayList<Player> patients = new ArrayList<>();

    public Hospital(String name, int index) {
        super(name, index);
    }

    @Override
    public Task interact(Player player) {
        if (patients.contains(player)) {
            return Task.CREATE_HOSPITAL_DIALOG;
        }
        return Task.NO_OP;
    }

    public void accept(Player patient) {
        patient.to(index);
        patient.immobilized = 5;
        patients.add(patient);
    }

    public void release(Player patient) {
        patient.immobilized = 0;
        patients.remove(patient);
    }

    @Override
    public String getTextureName() {
        return "hospital";
    }
}