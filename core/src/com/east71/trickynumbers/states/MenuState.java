package com.east71.trickynumbers.states;

import com.east71.trickynumbers.IActivityRequestHandler;
import com.east71.trickynumbers.models.MyButton;
import com.east71.trickynumbers.models.MyColor;
import com.east71.trickynumbers.models.RColor;
import com.east71.trickynumbers.models.TextButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

class MenuState extends State implements InputProcessor{
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
    private BitmapFont font, gameName;
    private Texture BGDark, BGWhite, soundOn, soundOff, star;
    private TextButton btnPlay;
//    private TextButton btnAbout, btnBG;
//    private ImageButton btnSwitchAD, btnSound;
    private ShapeRenderer shape;
    private Preferences pref;
    private MyColor myColor;

    private int h;
    private int w;
    private float gameNamePosY;
    private GlyphLayout glyphLayout;

    private boolean isBackKeyPressed;

    MenuState(GameStateManager gsm, AssetManager manager, IActivityRequestHandler handler) {
        super(gsm);
        this.handler = handler;
        //handler.showAds(false);
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
        myColor = RColor.getColor();
        pref.putString("COLOR", myColor.name);
        pref.flush();
        font = manager.get("small.ttf", BitmapFont.class);
        gameName = manager.get("gameName.ttf", BitmapFont.class);
        soundOn = manager.get("drawables/soundOn.png");
        soundOff = manager.get("drawables/soundOff.png");
        star = manager.get("drawables/star.png");
        shape = new ShapeRenderer();
        isBackKeyPressed = false;

        float PADDING = w / 10;
        float GAP = PADDING / 2;
        float buttonHeight = (int) (PADDING * 2.2f);
        float buttonWidth = (w - PADDING * 2 - GAP * 2) / 3;
        gameNamePosY = h - (h - (PADDING + GAP * 2 + buttonHeight * 3) )/ 2;

        BGDark = manager.get("drawables/BGDark.png");
        BGWhite = manager.get("drawables/BGWhite.png");
        btnPlay = new TextButton("PLAY", myColor, PADDING * 2, PADDING * 2 + GAP + buttonHeight, w - PADDING * 4, buttonHeight, font);
//        btnAbout = new TextButton("ABOUT", myColor, PADDING, PADDING, w - PADDING * 2, buttonHeight, font);
//
//        btnBG = new TextButton("BG", myColor, PADDING, PADDING + GAP + buttonHeight, buttonWidth, buttonHeight, font);
//        btnSound = new ImageButton(myColor, PADDING + GAP + buttonWidth, PADDING + buttonHeight + GAP,
//                buttonWidth, buttonHeight, pref.getInteger("SOUND", 1) == 1 ? soundOn : soundOff);
//        btnSwitchAD = new ImageButton(myColor, PADDING + GAP * 2 + buttonWidth * 2, PADDING + GAP + buttonHeight,
//                buttonWidth, buttonHeight, star);

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
        drawGameName(sb);
        renderButtons(sb, shape);
        if (isBackKeyPressed){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.1f, 0.1f, 0.1f, 0.9f);
            shape.rect(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2, w, h);
            shape.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            printText(sb, camera.position.x, camera.position.y + camera.viewportHeight / 6, "EXIT", Color.WHITE, font);
            printText(sb, camera.position.x, camera.position.y - camera.viewportHeight / 6, "CONTINUE", Color.WHITE, font);
        }
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
    private void drawGameName(SpriteBatch sb) {
        printText(sb, camera.position.x, gameNamePosY + gameName.getCapHeight() / 1.85f, "TRICKY", myColor.normal, gameName);
        if (pref.getInteger("BG", 0) == 0) {
            printText(sb, camera.position.x, gameNamePosY - gameName.getCapHeight() / 1.85f, "NUMBERS", Color.WHITE, gameName);
        }
        else {
            printText(sb, camera.position.x, gameNamePosY - gameName.getCapHeight() / 1.85f, "NUMBERS", Color.DARK_GRAY, gameName);
        }
    }
    private void renderButtons(SpriteBatch sb, ShapeRenderer shape){
        btnPlay.render(sb, shape);
//        btnAbout.render(sb, shape);
//        btnSwitchAD.render(sb, shape);
//        btnSound.render(sb, shape);
//        btnBG.render(sb, shape);
    }

    @Override
    public void dispose() {
        shape.dispose();
        btnPlay.dispose();
//        btnAbout.dispose();
//        btnBG.dispose();
//        btnSound.dispose();
//        btnSwitchAD.dispose();
        BGDark.dispose();
        BGWhite.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            isBackKeyPressed = !isBackKeyPressed;
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

        if(pointer < touches.size()){
            touches.get(pointer).touchX = v.x;
            touches.get(pointer).touchY = v.y;
            touches.get(pointer).touched = true;

            if (!isBackKeyPressed){
                if (btnPlay.contains(v)){
                    btnPlay.setPressed(true);
                    touches.get(pointer).button = btnPlay;
                }
//                if (btnAbout.contains(v)){
//                    btnAbout.setPressed(true);
//                    touches.get(pointer).button = btnAbout;
//                }
//                if (btnSwitchAD.contains(v)) {
//                    btnSwitchAD.setPressed(true);
//                    touches.get(pointer).button = btnSwitchAD;
//                }
//                if (btnSound.contains(v)) {
//                    btnSound.setPressed(true);
//                    touches.get(pointer).button = btnSound;
//                }
//                if (btnBG.contains(v)) {
//                    btnBG.setPressed(true);
//                    touches.get(pointer).button = btnBG;
//                }
            }
        }
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 v = normalize(screenX, screenY);

        if(pointer < touches.size()){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;

            if (isBackKeyPressed){
                if (v.y > camera.position.y) {
                    Gdx.app.exit();
                }
                else {
                    isBackKeyPressed = false;
                }
            }
            else{
                if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnPlay)){
                    btnPlay.setPressed(false);
                    if (btnPlay.contains(v)){
                        gsm.push(new com.east71.trickynumbers.states.ChooseDifficult(gsm, manager, handler));
                    }
                }
//                if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnAbout)){
//                    btnAbout.setPressed(false);
//                }
//                if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnSound)){
//                    btnSound.setPressed(false);
//                    if (btnSound.contains(v)) {
//                        switchSound();
//                    }
//                }
//                if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnBG)){
//                    btnBG.setPressed(false);
//                    if (btnBG.contains(v)) {
//                        changeBG();
//                    }
//                }
//                if (touches.get(pointer).button != null && touches.get(pointer).button.equals(btnSwitchAD)){
//                    btnSwitchAD.setPressed(false);
//                    if (btnSwitchAD.contains(v)) {
//                        switchAD_ENABLED();
//                    }
//                }
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

    private Vector2 normalize(int x, int y){
        return new Vector2(x * camera.viewportWidth / w, (h - y) * camera.viewportHeight / h);
    }

    private void printText(SpriteBatch sb, float posX, float posY, String text, Color color, BitmapFont font) {
        sb.begin();
        font.setColor(color);
        glyphLayout.setText(font, text);
        font.draw(sb, glyphLayout, posX - glyphLayout.width / 2 + glyphLayout.height / 20, posY + glyphLayout.height / 2);
        sb.end();
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
//    private void switchSound(){
//
//        if (pref.getInteger("SOUND", 1) == 0) {
//            pref.putInteger("SOUND", 1);
//            pref.flush();
//            btnSound.setTexture(soundOn);
//
//        }
//        else {
//            pref.putInteger("SOUND", 0);
//            pref.flush();
//            btnSound.setTexture(soundOff);
//        }
//    }
//    private void switchAD_ENABLED(){
//        if (pref.getBoolean("AD_ENABLED", true)) {
//            pref.putBoolean("AD_ENABLED", false);
//            pref.flush();
//
//        }
//        else {
//            pref.putBoolean("AD_ENABLED", true);
//            pref.flush();
//        }
//    }
}
