package com.picklegames.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.picklegames.game.FishGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new FishGame(), config);
		config.width = 1080;
		config.height = 720;
		config.resizable = false;
		config.title = "The Last Fish";
	}
}