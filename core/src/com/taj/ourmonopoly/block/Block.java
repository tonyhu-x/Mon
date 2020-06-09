package com.taj.ourmonopoly.block;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.taj.ourmonopoly.Player;

public abstract class Block {
    
    public static ArrayList<Block> getBlockList(String path) throws IOException {
        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(path));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
            System.exit(-1);
        }

        ArrayList<Block> blocks = new ArrayList<>(60);
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] tokens = row.split(",");
            int index = Integer.parseInt(tokens[0]);
            String name = tokens[1];
            String type = tokens[2];
            int group = Integer.parseInt(tokens[3]); 

            switch (type) {
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
                case "Property":
                    blocks.add(new Property(name, index, group));
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

    public abstract void interact(Player player);
    public abstract Vector2 getDimensions();
    public abstract String getImagePath();
}