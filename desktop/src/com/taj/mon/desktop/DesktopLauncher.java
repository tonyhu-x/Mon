package com.taj.mon.desktop;

import java.awt.Dimension;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.taj.mon.GameApp;

import Mon.BuildConfig;

public class DesktopLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Mon " + BuildConfig.VERSION);

		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		if (screenSize.getWidth() < 1.2 * GameApp.WIN_WIDTH_HIGH
			|| screenSize.getHeight() < 1.2 * GameApp.WIN_HEIGHT_HIGH)
		{
			config.setWindowedMode(GameApp.WIN_WIDTH_LOW, GameApp.WIN_HEIGHT_LOW);
		}
		else
            config.setWindowedMode(GameApp.WIN_WIDTH_HIGH, GameApp.WIN_HEIGHT_HIGH);
		config.setResizable(false);
		new Lwjgl3Application(new GameApp(), config);
	}
}
