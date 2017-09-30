package com.airse.trickynumbers.states;

import com.airse.trickynumbers.models.MyButton;
import com.airse.trickynumbers.models.MyColor;
import com.airse.trickynumbers.models.RColor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by qwert on 28.09.2017.
 */

public class MenuState extends State implements InputProcessor{

    private final BitmapFont gameName;
    private AssetManager manager;
    private BitmapFont font;
    private Texture bg;
    private MyButton btnPlay, btnSettings, btnHighscore;
    private ShapeRenderer shape;
    private Preferences pref;
    private int highScore;
    private MyColor myColor;

    private int h, w, tenth, buttonHeight, gameNamePosY;
    private int BUTTON_GAP;
    private int PADDING;
    private GlyphLayout glyphLayout;


    public MenuState(GameStateManager gsm, AssetManager manager) {
        super(gsm);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        camera.setToOrtho(false, w, h);
        camera.update();
        this.manager = manager;
        glyphLayout = new GlyphLayout();

        pref = Gdx.app.getPreferences("MY_PREFS");
        highScore = pref.getInteger("HIGHSCORE", 0);

        myColor = RColor.getColor();
        font = manager.get("small.ttf", BitmapFont.class);
        gameName = manager.get("gameName.ttf", BitmapFont.class);
        shape = new ShapeRenderer();

        tenth = w / 10;
        PADDING = tenth;
        BUTTON_GAP = tenth / 2;
        buttonHeight = (int)(PADDING * 2.2f);
        gameNamePosY = h - (h - (PADDING + BUTTON_GAP * 2 + buttonHeight * 3) )/ 2;

        bg = manager.get("drawables/bg.png", Texture.class);
        btnPlay = new MyButton("PLAY", 100, myColor, PADDING, PADDING + (BUTTON_GAP + buttonHeight) * 2, w - PADDING * 2, buttonHeight, font);
        btnSettings = new MyButton("SETTINGS", 100, myColor, PADDING, PADDING + BUTTON_GAP + buttonHeight, w - PADDING * 2, buttonHeight, font);
        btnHighscore = new MyButton("BEST: " + highScore, 100, myColor, PADDING, PADDING, w - PADDING * 2, buttonHeight, font);

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void resize(int width, int height) {

        camera.viewportHeight = h;
        camera.viewportWidth = camera.viewportHeight * width / height;
        camera.update();

        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
    }

    @Override
    public void update(float dt) {
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - camera.viewportWidth / 2 - w / 10, camera.position.y - camera.viewportHeight / 2,
                h * bg.getWidth() / bg.getHeight(), h);

        sb.end();
        printText(sb, camera.position.x, gameNamePosY + gameName.getCapHeight() / 1.85f, 0, "TRICKY", myColor.a400, gameName);
        printText(sb, camera.position.x, gameNamePosY - gameName.getCapHeight() / 1.85f, 0, "NUMBERS", Color.DARK_GRAY, gameName);



        renderButtons(sb, shape);

    }

    @Override
    public void dispose() {
        shape.dispose();
        btnPlay.dispose();
        btnSettings.dispose();
        btnHighscore.dispose();
    }

    private void renderButtons(SpriteBatch sb, ShapeRenderer shape){
        btnPlay.render(sb, shape);
        btnSettings.render(sb, shape);
        btnHighscore.render(sb, shape);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            Gdx.app.exit();
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
        if (btnPlay.contains(v)){
            btnPlay.setPressed(true);

        }
        if (btnSettings.contains(v)){
            btnSettings.setPressed(true);
        }
        if (btnHighscore.contains(v)){
            btnHighscore.setPressed(true);
        }
        Gdx.app.log("touch pos", v.toString());
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 v = normalize(screenX, screenY);
        btnPlay.setPressed(false);
        btnSettings.setPressed(false);
        btnHighscore.setPressed(false);
        if (btnPlay.contains(v)){
            gsm.push(new PlayState(gsm, manager));
        }
        Gdx.app.log("touch pos", v.toString());
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
    private Vector2 normalize(int x, int y){
        return new Vector2(x * camera.viewportWidth / w, (h - y) * camera.viewportHeight / h);
    }

    private void printText(SpriteBatch sb, float posX, float posY, float angle, String text, Color color, BitmapFont font) {
        Matrix4 oldTransformMatrix = sb.getTransformMatrix().cpy();

        Matrix4 mx4Font = new Matrix4();
        mx4Font.rotate(new Vector3(0, 0, 1), angle);
        mx4Font.trn(posX, posY, 0);
        sb.setTransformMatrix(mx4Font);

        sb.begin();
        font.setColor(color);
        glyphLayout.setText(font, text);
        float winnerTextHeight = glyphLayout.height;
        float winnerTextWidth = glyphLayout.width - (winnerTextHeight / 10);
        font.draw(sb, glyphLayout, - (winnerTextWidth / 2), winnerTextHeight / 2);
        sb.end();

        sb.setTransformMatrix(oldTransformMatrix);
    }
}
