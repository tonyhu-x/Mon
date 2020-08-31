package com.taj.mon;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureInventory {

    private static final TextureAtlas TEXTURE = new TextureAtlas(Gdx.files.internal("blocks.atlas"));
    private static final TextureAtlas TOKENS = new TextureAtlas(Gdx.files.internal("tokens.atlas"));
    private static final TextureAtlas DICE = new TextureAtlas(Gdx.files.internal("dice.atlas"));

    private static HashMap<String, TextureRegion> regions = new HashMap<>();

    public static TextureRegion getRegion(String name) {
        return get(TEXTURE, name);
    }

    public static TextureRegion getToken(String name) {
        return get(TOKENS, name);
    }

    public static TextureAtlas getDiceAtlas() {
        return DICE;
    }

    /**
     * Retrieves a dice texture based on the parameters.
     * 
     * @param whichColor  0-6
     * @param whichNumber 1-6
     * @return the corresponding image
     */
    public static TextureRegion getDiceRegion(int whichColor, int whichNumber) {
        String name = "d" + Integer.toString(whichColor) + Integer.toString(whichNumber);
        return get(DICE, name);
    }

    private static TextureRegion get(TextureAtlas atlas, String name) {
        if (regions.containsKey(name)) {
            return regions.get(name);
        }
        else {
            var temp = atlas.findRegion(name);
            regions.put(name, temp);
            return temp;
        }
    }
}