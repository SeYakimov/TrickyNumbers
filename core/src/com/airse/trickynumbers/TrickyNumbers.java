package com.airse.trickynumbers;

import com.airse.trickynumbers.states.GameStateManager;
import com.airse.trickynumbers.states.LoadingState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrickyNumbers extends ApplicationAdapter {

	public static final int WIDTH = 320;
	public static final int HEIGHT = 480;
	public static final String TITLE = "Tricky Numbers";

	public static final String PINK400 = "f50057";
	public static final String YELLOW400 = "ffea00";
	public static final String YELLOW700 = "ffd600";
	public static final String TEAL400 = "1de9b6";
	public static final String TEAL700 = "00bfa5";
	public static final String DEEPPURPLE400 = "651fff";
	public static final String PURPLE400 = "ab47bc";
	public static final String LIGHT_BLUE700 = "0091ea";
	public static final String LIME700 = "aeea00";
	public static final String GREAN400 = "00e676";


	private GameStateManager gsm;
	private SpriteBatch sb;
	private AssetManager manager;


	@Override
	public void create () {
		System.out.println("Та-дам");
		sb = new SpriteBatch();
		gsm = new GameStateManager();
		manager = new AssetManager();
		Gdx.gl.glClearColor(0.05f, 0.1f, 0.15f, 1);
		gsm.push(new LoadingState(manager, gsm));

	}
	@Override
	public void resize(int width, int height){
		gsm.resize(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(sb);
	}
	
	@Override
	public void dispose () {
		sb.dispose();
		manager.dispose();
	}
}
