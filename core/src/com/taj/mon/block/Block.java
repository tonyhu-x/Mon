package com.taj.mon.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.taj.mon.GameInstance.Task;
import com.taj.mon.Player;

public abstract class Block {

    public static ArrayList<Block> getBlockList(String path) throws IOException {
        BufferedReader csvReader = null;
        csvReader = new BufferedReader(new InputStreamReader(FileHandle.class.getResourceAsStream("/" + path)));

        ArrayList<Block> blocks = new ArrayList<>(60);
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] tokens = row.split(",");
            int index = Integer.parseInt(tokens[0]);
            String name = tokens[1];
            String type = tokens[2];
            int group = Integer.parseInt(tokens[3]);

            if (type.equals("Property")) {
                int purchasePrice = Integer.parseInt(tokens[4]);
                int[] rent = new int[5];
                for (int i = 0; i < 5; i++) {
                    rent[i] = Integer.parseInt(tokens[5 + i]);
                }

                blocks.add(new Property(name, index, group, purchasePrice, rent));
            }

            switch (type) {
                case "Property":
                    break;
                case "Bank":
                    blocks.add(new Bank(name, index));
                    break;
                case "Chance":
                    blocks.add(new Chance(name, index));
                    break;
                case "ComChest":
                    blocks.add(new ComChest(name, index));
                    break;
                case "Go":
                    blocks.add(new Go(name, index));
                    break;
                case "Hospital":
                    blocks.add(new Hospital(name, index));
                    break;
                case "Jail":
                    blocks.add(new Jail(name, index));
                    break;
                case "Metro":
                    blocks.add(new Metro(name, index));
                    break;
                case "Park":
                    blocks.add(new Park(name, index));
                    break;
                default:
                    break;
            }

        }

        csvReader.close();
        return blocks;
    }

    String name;

    /**
     * The index of this property on the map.
     */
    int index;
    
    public Block(String name, int index) {
        this.name = name;
        this.index = index;
    }
    
    public abstract Task interact(Player player);
    public abstract Vector2 getDimensions();
    public abstract String getTextureName();
    
    public String getName() {
        return name;
    }
}