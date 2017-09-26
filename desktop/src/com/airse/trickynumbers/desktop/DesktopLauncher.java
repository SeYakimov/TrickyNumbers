package com.airse.trickynumbers.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.airse.trickynumbers.TrickyNumbers;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TrickyNumbers.WIDTH;
		config.height = TrickyNumbers.HEIGHT;
		config.title = TrickyNumbers.TITLE;
		new LwjglApplication(new TrickyNumbers(), config);
	}
}
