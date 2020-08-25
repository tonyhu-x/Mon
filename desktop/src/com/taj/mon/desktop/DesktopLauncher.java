package com.taj.mon.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.taj.mon.GameApp;

public class DesktopLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Mon " + GameApp.VER_MAJOR + "." + GameApp.VER_MINOR + "." + GameApp.VER_PATCH);
		config.setWindowedMode(GameApp.WINDOW_WIDTH, GameApp.WINDOW_HEIGHT);
		config.setResizable(false);
		new Lwjgl3Application(new GameApp(), config);
	}
}
