package com.taj.ourmonopoly;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureInventory {

    private static final TextureAtlas TEXTURE = new TextureAtlas(Gdx.files.internal("blocks.atlas"));
    
    private static HashMap<String, TextureRegion> regions = new HashMap<>();

    public static TextureRegion getRegion(String name) {
        if (regions.containsKey(name)) {
            return regions.get(name);
        }
        else {
            var temp = TEXTURE.findRegion(name);
            regions.put(name, temp);
            return temp;
        }
    }
}