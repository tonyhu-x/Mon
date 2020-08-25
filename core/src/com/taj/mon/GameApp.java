package com.taj.mon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameApp extends Game {

	public static final int VER_MAJOR = 1;
	public static final int VER_MINOR = 0;
	public static final int VER_PATCH = 3;
	public static final int WIN_WIDTH_HIGH = 1920;
	public static final int WIN_HEIGHT_HIGH = 1080;
	public static final int WIN_WIDTH_LOW = 1280;
	public static final int WIN_HEIGHT_LOW = 720;
	public static final float ASPECT_RATIO = (float) WIN_HEIGHT_HIGH / WIN_WIDTH_HIGH;
	public static final String PATH_TO_ASSETS = "core/assets/";
	
	SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new TitleScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
        batch.dispose();
	}
}
