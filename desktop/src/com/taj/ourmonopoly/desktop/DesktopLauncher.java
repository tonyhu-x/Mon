package com.taj.ourmonopoly.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.taj.ourmonopoly.GameApp;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Monopoly Game " + GameApp.VER_MAJOR + "." + GameApp.VER_MINOR;
		config.width = GameApp.WINDOW_WIDTH;
		config.height = GameApp.WINDOW_HEIGHT;
		config.fullscreen = false;
		config.resizable = false;
		new LwjglApplication(new GameApp(), config);
	}
}
