package com.taj.ourmonopoly.block;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.taj.ourmonopoly.Player;

public abstract class Block {
    
    public static final int MAP_SIZE = 80;
    public static ArrayList<Block> blocks = new ArrayList<>(60);

    public static void queryBlock(Player player, int pos) {
        if (pos < 53) {
            blocks.get(pos).interact(player);
        }
        else if (pos < 63) {
            blocks.get(80 - pos).interact(player);
        }
        else if (pos < 67) {
            blocks.get(pos - 10).interact(player);
        }
        else if (pos < 77) {
            blocks.get(90 - pos).interact(player);
        }
        else {
            blocks.get(pos - 20).interact(player);
        }
    }


    public static void getBlockList(String path) throws IOException {
        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(path));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
            System.exit(-1);
        }

        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] tokens = row.split(",");
            int index = Integer.parseInt(tokens[0]);
            String name = tokens[1];
            String type = tokens[2];
            int group = Integer.parseInt(tokens[3]); 

            switch (type) {
                case "Property":
                    blocks.add(new Property(name, index, group));
                    break;
                case "Chance":
                    blocks.add(new Chance(name, index));
                    break;
                case "ComChest":
                    blocks.add(new ComChest(name, index));
                    break;
                default:
                    break;
            }

        }

        csvReader.close();
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

    public abstract void interact(Player player);
    public abstract Vector2 getDimensions();
    public abstract String getImagePath();
}