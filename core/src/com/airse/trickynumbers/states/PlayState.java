package com.airse.trickynumbers.states;

import com.airse.trickynumbers.IActivityRequestHandler;
import com.airse.trickynumbers.models.GameButton;
import com.airse.trickynumbers.models.MyColor;
import com.airse.trickynumbers.models.RColor;
import com.airse.trickynumbers.models.TextButton;
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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class PlayState extends State implements InputProcessor {


    private enum State{NEW_GAME, RUNNING, GAMEOVER, MOVE_LEFT, MOVE_RIGHT, PAUSE}
    private long startTime;
    private int h, w, tenth, buttonHeight,buttonHeightGameOver, buttonHeightScore;
    private int GAP;
    private Vector2 PADDING;
    private int PADDING_GAMEOVER;
    private GameButton btnTop, btnMiddle, btnBottom;
    private TextButton btnGameOver, btnScore, btnHighScore;
    private int counter, score;
    private BitmapFont normal, small, big;

    private State state, currentState;
    private ShapeRenderer shape;
    private Preferences pref;
    private boolean flag;
    private GlyphLayout glyphLayout;
    private AssetManager manager;
    private IActivityRequestHandler handler;
    private MyColor myColor;

    private int originSpeed;
    private float speed;
    private int distance;
    private int start, finish;
    private boolean isFirstTime, doCount, showAd;
    private int adCounter, AD_FREQUENCY;
    private boolean AD_ENABLED;

    private Texture BGDark, BGWhite;

    public PlayState(GameStateManager gsm, AssetManager manager, IActivityRequestHandler handler) {
        super(gsm);
        this.manager = manager;
        this.handler = handler;
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        tenth = w / 10;
        camera.setToOrtho(false, w, h);
        camera.update();
        distance = w;
        isFirstTime = true;
        originSpeed = (int)(w * 1.5f);
        speed = originSpeed;
        this.manager = manager;

        pref = Gdx.app.getPreferences("MY_PREFS");
        glyphLayout = new GlyphLayout();
        score = 0;
        flag = true;
        adCounter = 1;
        AD_FREQUENCY = 4;
        AD_ENABLED = pref.getBoolean("AD_ENABLED", true);
        doCount = true;
        showAd = false;
        shape = new ShapeRenderer();
        state = State.NEW_GAME;
        currentState = state;
        PADDING = new Vector2(tenth * 3, tenth); // 1) left-right, 2) top-bottom
        GAP = tenth / 2;
        buttonHeightGameOver = (h - ((int)PADDING.y + GAP) * 2) / 6;
        buttonHeightScore = h - (int)((buttonHeightGameOver + PADDING.y + GAP) * 2 + PADDING.x);
        PADDING_GAMEOVER = (h - buttonHeightScore - buttonHeightGameOver * 2 - GAP * 2) / 2;
        buttonHeight = (h - ((int)PADDING_GAMEOVER + GAP) * 2) / 3;
                normal = manager.get("normal.ttf", BitmapFont.class);
        small = manager.get("small.ttf", BitmapFont.class);
        big = manager.get("big.ttf", BitmapFont.class);

        BGDark = manager.get("drawables/BGDark.png");
        BGWhite = manager.get("drawables/BGWhite.png");

        myColor = RColor.getColor(pref.getString("COLOR"));
        btnTop = new GameButton("2", myColor, (int)PADDING.x,
                (int)PADDING_GAMEOVER + 2 * (GAP + buttonHeight),
                w - (int)PADDING.x * 2, buttonHeight, normal);
        btnMiddle = new GameButton("1", myColor, (int)PADDING.x, (int)PADDING_GAMEOVER + GAP + buttonHeight,
                w - (int)PADDING.x * 2, buttonHeight, normal);
        btnBottom = new GameButton("3", myColor, (int)PADDING.x, (int)PADDING_GAMEOVER,
                w - (int)PADDING.x * 2, buttonHeight, normal);

        btnGameOver = new TextButton("GAMEOVER", myColor, -w + (int)PADDING.y,
                PADDING_GAMEOVER + GAP * 2 + buttonHeightGameOver + buttonHeightScore, w - (int)PADDING.y * 2, buttonHeightGameOver, small);
        btnScore = new TextButton("", myColor, -w + (int)PADDING.y,
                PADDING_GAMEOVER + GAP + buttonHeightGameOver, w - (int)PADDING.y * 2, buttonHeightScore, big);
        btnHighScore = new TextButton("", myColor, -w + (int)PADDING.y,
                PADDING_GAMEOVER, w - (int)PADDING.y * 2, buttonHeightGameOver, small);
        newGame();
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
        switch (state) {
            case NEW_GAME:
                if (doCount) {
                    adCounter++;
                    doCount = false;
                }
                if (AD_ENABLED && showAd && adCounter % AD_FREQUENCY == 0) {
                    handler.showAds(true);
                    showAd = false;
                }
                break;
            case RUNNING:
                if (!doCount){
                    doCount = true;
                }
                if (AD_ENABLED && !showAd){
                    showAd = true;
                }
                if (btnBottom.check()) {
                    gameOver();
                }
                if (btnMiddle.check()) {
                    gameOver();
                }
                if (btnTop.check()) {
                    gameOver();
                }
                btnTop.update(dt);
                btnMiddle.update(dt);
                btnBottom.update(dt);
                break;
            case GAMEOVER:
                break;
            case MOVE_LEFT:
                if (flag) {
                    btnScore.setText("" + score);
                    btnHighScore.setText(updateHighScore()? "HIGHSCORE" : "BEST: " + getHighScore());
                    flag = false;
                }
                camera.position.x = Math.max(camera.position.x - dt * speed, finish);
                if (camera.position.x == finish) {
                    state = State.GAMEOVER;
                }
                break;
            case MOVE_RIGHT:
                camera.position.x = Math.min(camera.position.x + dt * speed, finish);
                if (camera.position.x == finish) {
                    state = State.NEW_GAME;
                }
                break;
            default:
                break;
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);
        drawBG(sb);
        switch (state) {
            case NEW_GAME:
                renderButtons(sb, shape);

                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(0.1f, 0.1f, 0.1f, 0.9f);
                shape.rect(0, 0, w, h);
                shape.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
                printText(sb, camera.position.x, camera.position.y, 0, "TAP TO PLAY", Color.WHITE, small);
                break;
            case RUNNING:
                renderButtons(sb, shape);
                break;
            case GAMEOVER:
                renderButtons(sb, shape);
                break;
            case MOVE_LEFT:
                renderButtons(sb, shape);
                break;
            case MOVE_RIGHT:
                renderButtons(sb, shape);
                break;
            case PAUSE:
                renderButtons(sb, shape);

                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(0.1f, 0.1f, 0.1f, 0.9f);
                shape.rect(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2, w, h);
                shape.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
                printText(sb, camera.position.x, camera.position.y + camera.viewportHeight / 6, 0, "GO TO MENU", Color.WHITE, small);
                printText(sb, camera.position.x, camera.position.y - camera.viewportHeight / 6, 0, "CONTINUE", Color.WHITE, small);
                break;
        }
        printText(sb, 50, 50, 0, "" + adCounter, Color.WHITE, small);
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
    private void renderButtons(SpriteBatch sb, ShapeRenderer shape) {
        btnBottom.render(sb, shape);
        btnMiddle.render(sb, shape);
        btnTop.render(sb, shape);
        btnGameOver.render(sb, shape);
        btnScore.render(sb, shape);
        btnHighScore.render(sb, shape);
    }
    @Override
    public void dispose() {
        btnBottom.dispose();
        btnMiddle.dispose();
        btnTop.dispose();
        btnHighScore.dispose();
        btnScore.dispose();
        btnGameOver.dispose();
        big.dispose();
        normal.dispose();
        small.dispose();
        shape.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            if (state == State.PAUSE) {
                state = currentState;
            }
            else {
                currentState = state;
                state = State.PAUSE;
            }
        }

        if(keycode == Input.Keys.ESCAPE){
            if (state == State.PAUSE) {
                state = currentState;
            }
            else {
                currentState = state;
                state = State.PAUSE;
            }
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
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 v = normalize(screenX, screenY);
        switch (state){
            case NEW_GAME:
                break;
            case RUNNING:
                if (btnBottom.contains(v)){
                    btnBottom.setPressed(true);
                    check(btnBottom);
                }
                if (btnMiddle.contains(v)){
                    btnMiddle.setPressed(true);
                    check(btnMiddle);
                }
                if (btnTop.contains(v)){
                    btnTop.setPressed(true);
                    check(btnTop);
                }
                break;
            case GAMEOVER:
                if (System.currentTimeMillis() - startTime > 500) {
                    if (btnGameOver.contains(v)){
                        btnGameOver.setPressed(true);
                    }
                    if (btnScore.contains(v)){
                        btnScore.setPressed(true);
                    }
                    if (btnHighScore.contains(v)){
                        btnHighScore.setPressed(true);
                    }
                }
                break;
            default:
                break;
        }
        Gdx.app.log("state", state.toString());
        return true;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 v = normalize(screenX, screenY);
        switch (state){
            case NEW_GAME:
                state = State.RUNNING;
                break;
            case RUNNING:
                btnBottom.setPressed(false);
                btnMiddle.setPressed(false);
                btnTop.setPressed(false);
                break;
            case GAMEOVER:
                btnBottom.setPressed(false);
                btnMiddle.setPressed(false);
                btnTop.setPressed(false);
                btnGameOver.setPressed(false);
                btnScore.setPressed(false);
                btnHighScore.setPressed(false);
                if (System.currentTimeMillis() - startTime > 500) {
                    newGame();
                }
                break;
            case PAUSE:
                if (v.y > camera.position.y) {
                    updateHighScore();
                    gsm.push(new MenuState(gsm, manager, handler));
                }
                else {
                    state = currentState;
                }
                break;
            default:
                break;
        }
        return true;
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
        switch (state) {
            case GAMEOVER:
                return new Vector2(x * camera.viewportWidth / w - camera.viewportWidth, (h - y) * camera.viewportHeight / h);
            default:
                return new Vector2(x * camera.viewportWidth / w, (h - y) * camera.viewportHeight / h);
        }
    }

    private boolean isRightAnswer(GameButton button){
        return button.getNumber() == counter;
    }

    private void updateButtons(int n, int m) {
        Random rand = new Random();
        int top = 0;
        int middle = 0;
        int bottom = 0;
        while(top != counter && middle != counter && bottom != counter) {
            top = rand.nextInt(n) + counter + m;
            middle = rand.nextInt(n) + counter + m;
            while (middle == top) {
                middle = rand.nextInt(n) + counter + m;
            }
            bottom = rand.nextInt(n) + counter + m;
            while (bottom == top || bottom == middle) {
                bottom = rand.nextInt(n) + counter + m;
            }
        }
        btnTop.setNumber(top);
        btnMiddle.setNumber(middle);
        btnBottom.setNumber(bottom);

        btnTop.setBoundsToOriginPosition();
        btnMiddle.setBoundsToOriginPosition();
        btnBottom.setBoundsToOriginPosition();
    }

    private void check(GameButton btn) {
        if (isRightAnswer(btn)){
            counter++;
            score++;
            updateButtons(4, -1);
            if (counter % 5 == 0) {
                increaseSpeed();
            }
        }
        else {
            gameOver();
        }
    }

    private void newGame(){
        counter = 1;
        score = 0;
        updateButtons(3, 0);
        setSpeedToOrigin();
        if (!isFirstTime){
            moveRight();
            state = State.MOVE_RIGHT;
            changeButtonsColor();
        }
        else {
            isFirstTime = false;
        }
    }
    private void gameOver() {
        startTime = System.currentTimeMillis();
        state = State.MOVE_LEFT;
        flag = true;
        moveLeft();
    }

    private boolean updateHighScore(){
        if (pref.getInteger("HIGHSCORE", 0) < score) {
            pref.putInteger("HIGHSCORE", score);
            pref.flush();
            return true;
        }
        return false;
    }
    private int getHighScore(){
        return pref.getInteger("HIGHSCORE", 0);
    }

    private void increaseSpeed(){
        btnTop.increaseSpeed();
        btnMiddle.increaseSpeed();
        btnBottom.increaseSpeed();
    }
    private void setSpeedToOrigin(){
        btnTop.setSpeedToOrigin();
        btnMiddle.setSpeedToOrigin();
        btnBottom.setSpeedToOrigin();
    }

    public void moveRight(int distance){
        this.distance = distance;
        moveRight();
    }
    private void moveRight(){
        start = (int)camera.position.x;
        finish = start + distance;
    }
    public void moveLeft( int distance){
        this.distance = distance;
        moveLeft();
    }
    private void moveLeft(){
        start = (int)camera.position.x;
        finish = start - distance;
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

    private void changeButtonsColor(){
        myColor = RColor.getColor();
        btnTop.changeColor(myColor);
        btnMiddle.changeColor(myColor);
        btnBottom.changeColor(myColor);
        btnGameOver.changeColor(myColor);
        btnScore.changeColor(myColor);
        btnHighScore.changeColor(myColor);
    }

}
