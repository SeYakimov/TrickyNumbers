package com.airse.trickynumbers.states;

import com.airse.trickynumbers.TrickyNumbers;
import com.airse.trickynumbers.models.MyButton;
import com.airse.trickynumbers.models.MyColor;
import com.airse.trickynumbers.models.RColor;
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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.sun.corba.se.spi.copyobject.CopyobjectDefaults;

import java.util.Random;

public class PlayState extends State implements InputProcessor {

    private enum State{NEW_GAME, RUNNING, GAMEOVER, MOVE_LEFT, MOVE_RIGHT, PAUSE}
    private long startTime;
    private int h, w, tenth, buttonHeight,buttonHeightGameOver ;
    private int BUTTON_GAP;
    private Vector2 PADDING;
    private MyButton buttonTop, buttonMiddle, buttonBottom;
    private MyButton btnGameOver, btnScore, btnHighScore;
    private int counter, score;
    private BitmapFont normal, small, big;
    private State state, currentState;
    private ShapeRenderer shape;
    private Preferences pref;
    private boolean flag;
    private GlyphLayout glyphLayout;
    private AssetManager manager;

    private MyColor red;
    private MyColor pink;
    private MyColor purple;
    private MyColor indigo;
    private MyColor light_blue;
    private MyColor cyan;
    private MyColor teal;
    private MyColor light_green;
    private MyColor lime;
    private MyColor yellow;
    private MyColor amber;
    private MyColor orange;
    private Array<MyColor> colors;
    private MyColor myColor;

    private int originSpeed;
    private float speed;
    private int distance;
    private int start, finish;
    private boolean isFirstTime;

    private Texture bg;

    public PlayState(GameStateManager gsm, AssetManager manager) {
        super(gsm);
        this.manager = manager;
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
        myColor = RColor.getColor();

        glyphLayout = new GlyphLayout();
        score = 0;
        flag = true;
        pref = Gdx.app.getPreferences("MY_PREFS");
        shape = new ShapeRenderer();
        state = State.NEW_GAME;
        currentState = state;
        PADDING = new Vector2(tenth * 3, tenth); // 1) left-right, 2) top-bottom
        BUTTON_GAP = tenth / 2;
        buttonHeight = (h - ((int)PADDING.y + BUTTON_GAP) * 2) / 3;
        buttonHeightGameOver = (h - ((int)PADDING.y + BUTTON_GAP) * 2) / 6;
        normal = manager.get("normal.ttf", BitmapFont.class);
        small = manager.get("small.ttf", BitmapFont.class);
        big = manager.get("big.ttf", BitmapFont.class);
//        int textSize = (int) (w * 0.25f);
//        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
//                Gdx.files.internal("fonts/FFFForward.ttf")
//        );
//        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter =
//                new FreeTypeFontGenerator.FreeTypeFontParameter();
//        freeTypeFontParameter.size = textSize;
//        freeTypeFontParameter.incremental = true;
//        fontGenerator.generateData(freeTypeFontParameter);
//        normal = fontGenerator.generateFont(freeTypeFontParameter);
//
//        textSize = (int)(w * 0.09f);
//        freeTypeFontParameter.size = textSize;
//        small = fontGenerator.generateFont(freeTypeFontParameter);
//
//        textSize = (int)(w * 0.47f);
//        freeTypeFontParameter.size = textSize;
//        big = fontGenerator.generateFont(freeTypeFontParameter);

        bg = manager.get("drawables/bg.png");

        buttonTop = new MyButton("2", 100, myColor, (int)PADDING.x,
                (int)PADDING.y + 2 * (BUTTON_GAP + buttonHeight),
                w - (int)PADDING.x * 2, buttonHeight, normal);
        buttonMiddle = new MyButton("1", 100, myColor, (int)PADDING.x, (int)PADDING.y + BUTTON_GAP + buttonHeight,
                w - (int)PADDING.x * 2, buttonHeight, normal);
        buttonBottom = new MyButton("3", 100, myColor, (int)PADDING.x, (int)PADDING.y,
                w - (int)PADDING.x * 2, buttonHeight, normal);

        btnGameOver = new MyButton("GAMEOVER", 100, myColor, -w + (int)PADDING.y,
                (int)(PADDING.y + BUTTON_GAP * 2 + buttonHeightGameOver * 5), w - (int)PADDING.y * 2, buttonHeightGameOver, small);
        btnScore = new MyButton("", 100, myColor, -w + (int)PADDING.y,
                (int)(PADDING.y + BUTTON_GAP + buttonHeightGameOver), w - (int)PADDING.y * 2, buttonHeightGameOver * 4, big);
        btnHighScore = new MyButton("", 100, myColor, -w + (int)PADDING.y,
                (int)PADDING.y, w - (int)PADDING.y * 2, buttonHeightGameOver, small);
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
                break;
            case RUNNING:
                if (buttonBottom.check()) {
                    gameOver();
                }
                if (buttonMiddle.check()) {
                    gameOver();
                }
                if (buttonTop.check()) {
                    gameOver();
                }
                buttonTop.update(dt);
                buttonMiddle.update(dt);
                buttonBottom.update(dt);
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
        sb.begin();
        sb.draw(bg, camera.position.x - camera.viewportWidth / 2 - w / 10, camera.position.y - camera.viewportHeight / 2,
                h * bg.getWidth() / bg.getHeight(), h);
        sb.end();
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
    }
    private void renderButtons(SpriteBatch sb, ShapeRenderer shape) {
        buttonBottom.render(sb, shape);
        buttonMiddle.render(sb, shape);
        buttonTop.render(sb, shape);
        btnGameOver.render(sb, shape);
        btnScore.render(sb, shape);
        btnHighScore.render(sb, shape);
    }
    @Override
    public void dispose() {
        buttonBottom.dispose();
        buttonMiddle.dispose();
        buttonTop.dispose();
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
                if (buttonBottom.contains(v)){
                    buttonBottom.setPressed(true);
                    check(buttonBottom);
                }
                if (buttonMiddle.contains(v)){
                    buttonMiddle.setPressed(true);
                    check(buttonMiddle);
                }
                if (buttonTop.contains(v)){
                    buttonTop.setPressed(true);
                    check(buttonTop);
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
                buttonBottom.setPressed(false);
                buttonMiddle.setPressed(false);
                buttonTop.setPressed(false);
                break;
            case GAMEOVER:
                buttonBottom.setPressed(false);
                buttonMiddle.setPressed(false);
                buttonTop.setPressed(false);
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
                    gsm.push(new MenuState(gsm, manager));
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

    private boolean isRightAnswer(MyButton button){
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
        buttonTop.setNumber(top);
        buttonMiddle.setNumber(middle);
        buttonBottom.setNumber(bottom);

        buttonTop.setBoundsToOriginPosition();
        buttonMiddle.setBoundsToOriginPosition();
        buttonBottom.setBoundsToOriginPosition();
    }

    private void check(MyButton btn) {
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
        changeButtonsColor();
        counter = 1;
        score = 0;
        updateButtons(3, 0);
        setSpeedToOrigin();
        if (!isFirstTime){
            moveRight();
            state = State.MOVE_RIGHT;
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
        buttonTop.increaseSpeed();
        buttonMiddle.increaseSpeed();
        buttonBottom.increaseSpeed();
    }
    private void setSpeedToOrigin(){
        buttonTop.setSpeedToOrigin();
        buttonMiddle.setSpeedToOrigin();
        buttonBottom.setSpeedToOrigin();
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
        buttonTop.changeColor(myColor);
        buttonMiddle.changeColor(myColor);
        buttonBottom.changeColor(myColor);
        btnGameOver.changeColor(myColor);
        btnScore.changeColor(myColor);
        btnHighScore.changeColor(myColor);
    }

}
