package com.airse.trickynumbers.states;

import com.airse.trickynumbers.IActivityRequestHandler;
import com.airse.trickynumbers.models.MyButton;
import com.airse.trickynumbers.models.MyColor;
import com.airse.trickynumbers.models.RColor;
import com.airse.trickynumbers.models.TextButton;
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
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

class ChooseDifficult extends State implements InputProcessor {
    private class TouchInfo {
        float touchX = 0;
        float touchY = 0;
        boolean touched = false;
        MyButton button = null;
    }

    private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();

    private AssetManager manager;
    private IActivityRequestHandler handler;
    private boolean AD_ENABLED;
    private BitmapFont gameName;
    private Texture BGDark, BGWhite;
    private TextButton btnEASY, btnMEDIUM, btnHARD, btnEASYHigh, btnMEDIUMHigh, btnHARDHigh;
    private ShapeRenderer shape;
    private Preferences pref;
    private int highScore;
    private MyColor myColor;

    private int h;
    private int w;
    private float gameNamePosY;
    private GlyphLayout glyphLayout;

    ChooseDifficult(GameStateManager gsm, AssetManager manager, IActivityRequestHandler handler) {
        super(gsm);
        this.handler = handler;
        Gdx.input.setInputProcessor(this);
        for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }
        Gdx.input.setCatchBackKey(true);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        camera.setToOrtho(false, w, h);
        camera.update();
        this.manager = manager;
        glyphLayout = new GlyphLayout();

        pref = Gdx.app.getPreferences("MY_PREFS");
        AD_ENABLED = pref.getBoolean("AD_ENABLED", true);
        highScore = pref.getInteger("HIGHSCORE", 0);
        myColor = RColor.getColor(pref.getString("COLOR"));
        BitmapFont font = manager.get("small.ttf", BitmapFont.class);
        gameName = manager.get("gameName.ttf", BitmapFont.class);
        shape = new ShapeRenderer();

        float PADDING = w / 10;
        float GAP = PADDING / 2;
        float buttonHeight = (int) (PADDING * 2.2f);
        float buttonWidth = buttonHeight;
        float highScoresX = w - buttonWidth - PADDING;
        gameNamePosY = h - (h - (PADDING + GAP * 2 + buttonHeight * 3) )/ 2;

        BGDark = manager.get("drawables/BGDark.png");
        BGWhite = manager.get("drawables/BGWhite.png");

        btnEASY = new TextButton("EASY", myColor, PADDING,
                PADDING + 2 * (GAP + buttonHeight),
                w - PADDING * 2 - GAP - buttonWidth, buttonHeight, font);
        btnMEDIUM = new TextButton("MEDIUM", myColor, PADDING, PADDING + GAP + buttonHeight,
                w - PADDING * 2 - GAP - buttonWidth, buttonHeight, font);
        btnHARD = new TextButton("HARD", myColor, PADDING, PADDING,
                w - PADDING * 2 - GAP - buttonWidth, buttonHeight, font);

        btnEASYHigh = new TextButton("" + pref.getInteger("HIGHSCORE_EASY", 0), myColor, highScoresX,
                PADDING + 2 * (GAP + buttonHeight),
                buttonWidth, buttonHeight, font);
        btnMEDIUMHigh = new TextButton("" + pref.getInteger("HIGHSCORE_MEDIUM", 0), myColor, highScoresX, PADDING + GAP + buttonHeight,
                buttonWidth, buttonHeight, font);
        btnHARDHigh = new TextButton("" + pref.getInteger("HIGHSCORE_HARD", 0), myColor, highScoresX, PADDING,
                buttonWidth, buttonHeight, font);

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
        shape.setProjectionMatrix(camera.combined);
        drawBG(sb);
        renderButtons(sb, shape);
        drawGameName(sb);
    }

    private void drawBG(SpriteBatch sb) {
        sb.begin();
        if (pref.getInteger("BG", 0) == 0) {
            sb.draw(BGDark, camera.position.x - camera.viewportWidth / 2 - w / 10, camera.position.y - camera.viewportHeight / 2,
                    h * BGDark.getWidth() / BGDark.getHeight(), h);
        } else {
            sb.draw(BGWhite, camera.position.x - camera.viewportWidth / 2 - w / 10, camera.position.y - camera.viewportHeight / 2,
                    h * BGDark.getWidth() / BGDark.getHeight(), h);
        }
        sb.end();
    }
    private void drawGameName(SpriteBatch sb) {
        printText(sb, camera.position.x, gameNamePosY + gameName.getCapHeight() / 1.85f, "TRICKY", myColor.normal, gameName);
        if (pref.getInteger("BG", 0) == 0) {
            printText(sb, camera.position.x, gameNamePosY - gameName.getCapHeight() / 1.85f, "NUMBERS", Color.WHITE, gameName);
        }
        else {
            printText(sb, camera.position.x, gameNamePosY - gameName.getCapHeight() / 1.85f, "NUMBERS", Color.DARK_GRAY, gameName);
        }
    }
    private void renderButtons(SpriteBatch sb, ShapeRenderer shape) {
        btnEASY.render(sb, shape);
        btnMEDIUM.render(sb, shape);
        btnHARD.render(sb, shape);

        btnEASYHigh.render(sb, shape);
        btnMEDIUMHigh.render(sb, shape);
        btnHARDHigh.render(sb, shape);
    }
    @Override
    public void dispose() {
        shape.dispose();
        btnEASY.dispose();
        btnMEDIUM.dispose();
        btnHARD.dispose();
        btnEASYHigh.dispose();
        btnMEDIUMHigh.dispose();
        btnHARDHigh.dispose();
        BGDark.dispose();
        BGWhite.dispose();
    }
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            gsm.push(new MenuState(gsm, manager, handler));
        }
        if (keycode == Input.Keys.ESCAPE) {
            gsm.push(new MenuState(gsm, manager, handler));
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
        if(pointer < touches.size()) {
            touches.get(pointer).touchX = v.x;
            touches.get(pointer).touchY = v.y;
            touches.get(pointer).touched = true;

            if (btnEASY.contains(v)){
                btnEASY.setPressed(true);
                touches.get(pointer).button = btnEASY;
            }
            if (btnMEDIUM.contains(v)) {
                btnMEDIUM.setPressed(true);
                touches.get(pointer).button = btnMEDIUM;
            }
            if (btnHARD.contains(v)) {
                btnHARD.setPressed(true);
                touches.get(pointer).button = btnHARD;
            }
            if (btnEASYHigh.contains(v)) {
                btnEASYHigh.setPressed(true);
                touches.get(pointer).button = btnEASYHigh;
            }
            if (btnMEDIUMHigh.contains(v)) {
                btnMEDIUMHigh.setPressed(true);
                touches.get(pointer).button = btnMEDIUMHigh;
            }
            if (btnHARDHigh.contains(v)) {
                btnHARDHigh.setPressed(true);
                touches.get(pointer).button = btnHARDHigh;
            }
        }
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 v = normalize(screenX, screenY);

        if(pointer < touches.size()) {
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;


            if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnEASY)){
                btnEASY.setPressed(false);
                if (btnEASY.contains(v)) {
                    gsm.push(new PlayState(gsm, manager, handler, Difficulty.EASY));
                }
            }
            if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnMEDIUM)){
                btnMEDIUM.setPressed(false);
                if (btnMEDIUM.contains(v)){
                    gsm.push(new PlayState(gsm, manager, handler, Difficulty.MEDIUM));
                }
            }
            if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnHARD)){
                btnHARD.setPressed(false);
                if (btnHARD.contains(v)){
                    gsm.push(new PlayState(gsm, manager, handler, Difficulty.HARD));
                }
            }
            if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnEASYHigh)){
                btnEASYHigh.setPressed(false);
                if (btnEASYHigh.contains(v)) {
                }
            }
            if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnMEDIUMHigh)){
                btnMEDIUMHigh.setPressed(false);
                if (btnMEDIUMHigh.contains(v)) {
                }
            }
            if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnHARDHigh)){
                btnHARDHigh.setPressed(false);
                if (btnHARDHigh.contains(v)) {
                }
            }
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
    private void printText(SpriteBatch sb, float posX, float posY, String text, Color color, BitmapFont font) {
        sb.begin();
        font.setColor(color);
        glyphLayout.setText(font, text);
        font.draw(sb, glyphLayout, posX - glyphLayout.width / 2 + glyphLayout.height / 20, posY + glyphLayout.height / 2);
        sb.end();
    }
}
