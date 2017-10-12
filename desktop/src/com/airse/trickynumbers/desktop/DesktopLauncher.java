package com.airse.trickynumbers.desktop;

import com.airse.trickynumbers.IActivityRequestHandler;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.airse.trickynumbers.TrickyNumbers;

public class DesktopLauncher implements IActivityRequestHandler {
	private static DesktopLauncher application;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TrickyNumbers.WIDTH;
		config.height = TrickyNumbers.HEIGHT;
		config.title = TrickyNumbers.TITLE;
		if (application == null) {
			application = new DesktopLauncher();
		}
		new LwjglApplication(new TrickyNumbers(application), config);
	}

	@Override
	public void showAds(boolean show) {

	}
}
