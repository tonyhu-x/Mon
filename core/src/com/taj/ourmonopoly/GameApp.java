package com.taj.ourmonopoly;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameApp extends Game {

	public static final int VER_MAJOR = 0;
	public static final int VER_MINOR = 1;
	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	public static final float ASPECT_RATIO;

	static {
		ASPECT_RATIO = (float) WINDOW_HEIGHT / WINDOW_WIDTH;
	}	

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
