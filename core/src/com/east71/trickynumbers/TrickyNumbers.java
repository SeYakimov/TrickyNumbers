package com.east71.trickynumbers;

import com.east71.trickynumbers.states.GameStateManager;
import com.east71.trickynumbers.states.LoadingState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrickyNumbers extends ApplicationAdapter {
	public static final int WIDTH = 320;
	public static final int HEIGHT = 512;
	public static final String TITLE = "Tricky Numbers";

	private GameStateManager gsm;
	private SpriteBatch sb;
	private AssetManager manager;
	private IActivityRequestHandler myRequestHandler;

	public TrickyNumbers(IActivityRequestHandler handler){
		myRequestHandler = handler;
	}
	@Override
	public void create () {
		sb = new SpriteBatch();
		gsm = new GameStateManager();
		manager = new AssetManager();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		gsm.push(new LoadingState(manager, gsm, myRequestHandler));
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

	public IActivityRequestHandler getHandler() {
		return myRequestHandler;
	}
}
