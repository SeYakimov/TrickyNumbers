package com.airse.trickynumbers.states;

import com.airse.trickynumbers.models.MyButton;
import com.airse.trickynumbers.models.MyColor;
import com.airse.trickynumbers.models.RColor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by qwert on 03.10.2017.
 */

public class SettingsState extends State implements InputProcessor {

    private int w, h, PADDING, buttonHeight, GAP;
    private AssetManager manager;
    private Texture BGDark, BGWhite;
    private MyColor myColor;
    private MyButton btnTop, btnMiddle, btnBottom;
    private Preferences pref;
    private BitmapFont font;
    private Sound click;
    private ShapeRenderer shape;

    public SettingsState(GameStateManager gsm, AssetManager manager) {
        super(gsm);
        this.manager = manager;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        camera.setToOrtho(false, w, h);
        camera.update();

        PADDING = w / 10;
        buttonHeight = (int)(PADDING * 2.2f);
        GAP = PADDING / 2;
        BGDark = manager.get("drawables/BGDark.png", Texture.class);
        BGWhite = manager.get("drawables/BGWhite.png", Texture.class);
        click = manager.get("sounds/click.mp3", Sound.class);
        font = manager.get("small.ttf", BitmapFont.class);
        pref = Gdx.app.getPreferences("MY_PREFS");
        myColor = RColor.getColor();
        shape = new ShapeRenderer();

        btnBottom = new MyButton("BACKGROUND", 100, myColor, PADDING, PADDING, w - PADDING * 2, buttonHeight, font);
        btnMiddle = new MyButton(pref.getInteger("SOUND", 1) == 1? "SOUND ON" : "SOUND OFF", 100, myColor, PADDING, PADDING + buttonHeight + GAP, w - PADDING * 2, buttonHeight, font);
        btnTop = new MyButton(pref.getInteger("MUSIC", 1) == 1? "MUSIC ON" : "MUSIC OFF", 100, myColor, PADDING, PADDING + (buttonHeight + GAP) * 2, w - PADDING * 2, buttonHeight, font);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        drawBG(sb);
        btnTop.render(sb, shape);
        btnMiddle.render(sb, shape);
        btnBottom.render(sb, shape);
    }

    private void drawBG(SpriteBatch sb) {
        sb.begin();
        if (pref.getInteger("BG", 0) == 0) {
            sb.draw(BGDark, camera.position.x - camera.viewportWidth / 2 - w / 10, camera.position.y - camera.viewportHeight / 2,
                    h * BGDark.getWidth() / BGDark.getHeight(), h);
        }
        else {
            sb.draw(BGWhite, camera.position.x - camera.viewportWidth / 2 - w / 10, camera.position.y - camera.viewportHeight / 2,
                    h * BGDark.getWidth() / BGDark.getHeight(), h);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        shape.dispose();
        BGDark.dispose();
        BGWhite.dispose();
        font.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            gsm.push(new MenuState(gsm, manager));
        }
        if (keycode == Input.Keys.ESCAPE) {
            gsm.push(new MenuState(gsm, manager));
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 v = normalize(screenX, screenY);
        if (btnTop.contains(v)) {
            btnTop.setPressed(true);
            playClick();
        }
        if (btnMiddle.contains(v)) {
            btnMiddle.setPressed(true);
            playClick();
        }
        if (btnBottom.contains(v)) {
            btnBottom.setPressed(true);
            playClick();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 v = normalize(screenX, screenY);
        btnTop.setPressed(false);
        btnMiddle.setPressed(false);
        btnBottom.setPressed(false);
        if (btnTop.contains(v)) {
            switchMusic();
        }
        if (btnMiddle.contains(v)) {
            switchSound();
        }
        if (btnBottom.contains(v)) {
            changeBG();
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private Vector2 normalize(int x, int y) {
        return new Vector2(x * camera.viewportWidth / w, (h - y) * camera.viewportHeight / h);
    }

    private void changeBG() {
        if (pref.getInteger("BG", 0) == 0) {
            pref.putInteger("BG", 1);
            pref.flush();
        }
        else {
            pref.putInteger("BG", 0);
            pref.flush();
        }
    }

    private void switchSound(){

        if (pref.getInteger("SOUND", 1) == 0) {
            pref.putInteger("SOUND", 1);
            pref.flush();
            btnMiddle.setText("SOUND ON");
        }
        else {
            pref.putInteger("SOUND", 0);
            pref.flush();
            btnMiddle.setText("SOUND OFF");
        }
    }
    private void switchMusic(){

        if (pref.getInteger("MUSIC", 1) == 0) {
            pref.putInteger("MUSIC", 1);
            pref.flush();
            btnTop.setText("MUSIC ON");
        }
        else {
            pref.putInteger("MUSIC", 0);
            pref.flush();
            btnTop.setText("MUSIC OFF");
        }
    }
    private void playClick(){
        if (pref.getInteger("SOUND", 1) == 1) {
            click.play();
        }
    }
}
