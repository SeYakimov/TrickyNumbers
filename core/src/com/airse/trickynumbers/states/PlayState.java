package com.airse.trickynumbers.states;

import com.airse.trickynumbers.TrickyNumbers;
import com.airse.trickynumbers.models.MyButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class PlayState extends State implements InputProcessor {

    private enum State{NEW_GAME, RUNNING, GAMEOVER}
    private long startTime;
    private int h, w, tenth, buttonHeight,buttonHeightGameOver ;
    private int BUTTON_GAP;
    private Vector2 PADDING;
    private MyButton buttonTop, buttonMiddle, buttonBottom;
    private MyButton btnGameOver, btnScore, btnHighScore;
    private int counter, score;
    private BitmapFont normal, small, big;
    private State state;
    private ShapeRenderer shape;
    private Preferences pref;
    private boolean flag;

    private Texture bg;

//    private Stage stage;
//    private Skin skin;
//    private Button button1;
//    private Button.ButtonStyle style;

    public PlayState(GameStateManager gsm, AssetManager manager) {
        super(gsm);

        Gdx.input.setInputProcessor(this);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        tenth = w / 10;
        camera.setToOrtho(false, w, h);
        camera.update();
//        stage = new Stage();
//        Gdx.input.setInputProcessor(stage);
//
//        skin = new Skin();
//        skin.add("up", new Texture("drawables/ButtonNotPressed.png"));
//        skin.add("down", new Texture("drawables/ButtonPressed.png"));
//        style = new Button.ButtonStyle(skin.getDrawable("up"), skin.getDrawable("down"), skin.getDrawable("up"));
//        button1 = new Button(style);
//        button1.setPosition(32, 320);
//        button1.setSize(256, 128);
//        stage.addActor(button1);
        score = 0;
        flag = true;
        pref = Gdx.app.getPreferences("MY_PREFS");
        shape = new ShapeRenderer();
        state = State.NEW_GAME;
        PADDING = new Vector2(tenth * 3, tenth); // 1) left-right, 2) top-bottom
        BUTTON_GAP = tenth / 2;
        buttonHeight = (h - ((int)PADDING.y + BUTTON_GAP) * 2) / 3;
        buttonHeightGameOver = (h - ((int)PADDING.y + BUTTON_GAP) * 2) / 6;
//        normal = manager.get("normal.ttf", BitmapFont.class);
//        small = manager.get("small.ttf", BitmapFont.class);
//        big = manager.get("big.ttf", BitmapFont.class);
        int textSize = (int) (w * 0.25f);
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/FFFForward.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = textSize;
        freeTypeFontParameter.incremental = true;
        fontGenerator.generateData(freeTypeFontParameter);
        normal = fontGenerator.generateFont(freeTypeFontParameter);

        textSize = (int)(w * 0.09f);
        freeTypeFontParameter.size = textSize;
        small = fontGenerator.generateFont(freeTypeFontParameter);
        
        textSize = (int)(w * 0.47f);
        freeTypeFontParameter.size = textSize;
        big = fontGenerator.generateFont(freeTypeFontParameter);

        bg = manager.get("drawables/bg.png");

        buttonTop = new MyButton("2", 100, Color.valueOf(TrickyNumbers.PINK400), (int)PADDING.x,
                (int)PADDING.y + 2 * (BUTTON_GAP + buttonHeight),
                w - (int)PADDING.x * 2, buttonHeight, normal);
        buttonMiddle = new MyButton("1", 100, Color.valueOf(TrickyNumbers.LIME700), (int)PADDING.x, (int)PADDING.y + BUTTON_GAP + buttonHeight,
                w - (int)PADDING.x * 2, buttonHeight, normal);
        buttonBottom = new MyButton("3", 100, Color.valueOf(TrickyNumbers.PURPLE400), (int)PADDING.x, (int)PADDING.y,
                w - (int)PADDING.x * 2, buttonHeight, normal);

        if(btnGameOver == null) {
            btnGameOver = new MyButton("GAMEOVER", 100, Color.valueOf(TrickyNumbers.PINK400), (int)PADDING.y,
                    (int)(PADDING.y + BUTTON_GAP * 2 + buttonHeightGameOver * 5), w - (int)PADDING.y * 2, buttonHeightGameOver, small);
        }
        if(btnScore == null) {
            btnScore = new MyButton("" + (score), 100, Color.valueOf(TrickyNumbers.LIME700), (int)PADDING.y,
                    (int)(PADDING.y + BUTTON_GAP + buttonHeightGameOver), w - (int)PADDING.y * 2, buttonHeightGameOver * 4, big);
        }
        if(btnHighScore == null) {
            int highScore = getHighScore();
            String s = "BEST: " + highScore;
            btnHighScore = new MyButton(s, 100, Color.valueOf(TrickyNumbers.PURPLE400), (int)PADDING.y,
                    (int)PADDING.y, w - (int)PADDING.y * 2, buttonHeightGameOver, small);
        }
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

        camera.update();
        switch (state) {
            case NEW_GAME:
                break;
            case RUNNING:
                if (buttonBottom.check()) {
                    flag = true;
                    gameOver();
                }
                if (buttonMiddle.check()) {
                    flag = true;
                    gameOver();
                }
                if (buttonTop.check()) {
                    flag = true;
                    gameOver();
                }
                buttonTop.update(dt);
                buttonMiddle.update(dt);
                buttonBottom.update(dt);
                break;
            case GAMEOVER:
                boolean b = updateHighScore();
                if (flag) {
                    btnScore.setText("" + score);
                    btnHighScore.setText(b? "HIGHSCORE" : "BEST: " + getHighScore());
                    flag = false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.begin();
        sb.draw(bg, 0, 0, w, h);
        sb.end();


        sb.setProjectionMatrix(camera.combined);
        switch (state) {
            case NEW_GAME:
                renderButtons(sb);

                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(0.1f, 0.5f, 0.5f, 0.5f);
                shape.rect(0, 0, w, h);
                shape.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);

                break;
            case RUNNING:
                renderButtons(sb);

                break;
            case GAMEOVER:
                btnGameOver.render(sb);
                btnScore.render(sb);
                btnHighScore.render(sb);

                break;
        }

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

    private void renderButtons(SpriteBatch sb) {
        buttonBottom.render(sb);
        buttonMiddle.render(sb);
        buttonTop.render(sb);
    }

    @Override
    public boolean keyDown(int keycode) {
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

                    state = State.RUNNING;
                    newGame();
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
        return new Vector2(x * camera.viewportWidth / w, (h - y) * camera.viewportHeight / h);
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
        counter = 1;
        score = 0;
        updateButtons(3, 0);
        setSpeedToOrigin();
    }
    private void gameOver() {
        startTime = System.currentTimeMillis();
        state = State.GAMEOVER;
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
}
