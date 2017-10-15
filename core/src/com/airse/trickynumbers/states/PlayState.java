package com.airse.trickynumbers.states;

import com.airse.trickynumbers.IActivityRequestHandler;
import com.airse.trickynumbers.models.GameButton;
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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class PlayState extends State implements InputProcessor {

    private enum State{NEW_GAME, RUNNING, GAMEOVER, MOVE_LEFT, MOVE_RIGHT, PAUSE}
    private long startTime;
    private int h;
    private int w;
    private GameButton gButtons[];
    private TextButton btnGameOver, btnScore, btnHighScore;
    private int counter, score, numOfButtons;
    private BitmapFont normal, small, big;
    private Difficulty difficulty;

    private State state, currentState;
    private ShapeRenderer shape;
    private Preferences pref;
    private boolean flag;
    private GlyphLayout glyphLayout;
    private AssetManager manager;
    private IActivityRequestHandler handler;
    private MyColor myColor;

    private float speed;
    private int distance;
    private int start, finish;
    private boolean isFirstTime, doCount, showAd;
    private int adCounter, AD_FREQUENCY;
    private boolean AD_ENABLED;

    private Texture BGDark, BGWhite;

    PlayState(GameStateManager gsm, AssetManager manager, IActivityRequestHandler handler, Difficulty difficulty) {
        super(gsm);
        this.manager = manager;
        this.handler = handler;
        this.difficulty = difficulty;
        switch (difficulty){

            case EASY:
                numOfButtons = 2;
                break;
            case MEDIUM:
                numOfButtons = 3;
                break;
            case HARD:
                numOfButtons = 4;
                break;
        }
        gButtons = new GameButton[numOfButtons];
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        int tenth = w / 10;
        camera.setToOrtho(false, w, h);
        camera.update();
        distance = w;
        isFirstTime = true;
        speed = (int) (w * 1.5f);
        this.manager = manager;

        pref = Gdx.app.getPreferences("MY_PREFS");
        glyphLayout = new GlyphLayout();
        score = 0;
        flag = true;
        adCounter = 0;
        AD_FREQUENCY = 4;
        AD_ENABLED = pref.getBoolean("AD_ENABLED", true);
        doCount = true;
        showAd = false;
        shape = new ShapeRenderer();
        state = State.NEW_GAME;
        currentState = state;

        Vector2 PADDING = new Vector2(tenth * 3, tenth);
        float GAP = tenth / 2;
        float buttonHeightGameOver = (h - ((int) PADDING.y + GAP) * 2) / 6;
        float buttonHeightScore = h - (int) ((buttonHeightGameOver + PADDING.y + GAP) * 2 + PADDING.x);
        float PADDING_GAMEOVER = (h - buttonHeightScore - buttonHeightGameOver * 2 - GAP * 2) / 2;
        float buttonHeight = (h - (PADDING_GAMEOVER + GAP) * 2) / 3;
        float PADDING_BOTTOM = (h - numOfButtons * buttonHeight - (numOfButtons - 1) * GAP) / 2;

        normal = manager.get("normal.ttf", BitmapFont.class);
        small = manager.get("small.ttf", BitmapFont.class);
        big = manager.get("big.ttf", BitmapFont.class);
        myColor = RColor.getColor(pref.getString("COLOR"));

        BGDark = manager.get("drawables/BGDark.png");
        BGWhite = manager.get("drawables/BGWhite.png");

        for (int i = 0; i < numOfButtons; i++){
            gButtons[i] = new GameButton("", myColor, (int) PADDING.x, PADDING_BOTTOM + i * (GAP + buttonHeight),
                w - (int) PADDING.x * 2, buttonHeight, normal);
        }

        btnGameOver = new TextButton("GAMEOVER", myColor, -w + (int) PADDING.y,
                PADDING_GAMEOVER + GAP * 2 + buttonHeightGameOver + buttonHeightScore, w - (int) PADDING.y * 2, buttonHeightGameOver, small);
        btnScore = new TextButton("", myColor, -w + (int) PADDING.y,
                PADDING_GAMEOVER + GAP + buttonHeightGameOver, w - (int) PADDING.y * 2, buttonHeightScore, big);
        btnHighScore = new TextButton("", myColor, -w + (int) PADDING.y,
                PADDING_GAMEOVER, w - (int) PADDING.y * 2, buttonHeightGameOver, small);
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
                if (AD_ENABLED && showAd && (adCounter % AD_FREQUENCY == 0)) {
                    handler.showAds(true);
                    showAd = false;
                }
                if (AD_ENABLED && showAd && (adCounter % AD_FREQUENCY == 1)) {
                    handler.showAds(false);
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
                for (int i = 0; i < numOfButtons; i++){
                    if (gButtons[i].check()){
                        gameOver();
                    }
                    gButtons[i].update(dt);
                }
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
                printText(sb, camera.position.x, camera.position.y, "TAP TO PLAY", Color.WHITE, small);
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
                printText(sb, camera.position.x, camera.position.y + camera.viewportHeight / 6, "GO TO MENU", Color.WHITE, small);
                printText(sb, camera.position.x, camera.position.y - camera.viewportHeight / 6, "CONTINUE", Color.WHITE, small);
                break;
        }
        printText(sb, 50, 50, "" + adCounter, Color.WHITE, small);
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
        for (int i = 0; i < numOfButtons; i++){
            gButtons[i].render(sb, shape);
        }
        btnGameOver.render(sb, shape);
        btnScore.render(sb, shape);
        btnHighScore.render(sb, shape);
    }
    @Override
    public void dispose() {
        for (int i = 0; i < numOfButtons; i++){
            gButtons[i].dispose();
        }
        btnHighScore.dispose();
        btnScore.dispose();
        btnGameOver.dispose();
        big.dispose();
        normal.dispose();
        small.dispose();
        shape.dispose();
        BGDark.dispose();
        BGWhite.dispose();
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
                for (int i = 0; i < numOfButtons; i++){
                    if (gButtons[i].contains(v)){
                        gButtons[i].setPressed(true);
                        check(gButtons[i]);
                    }
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
                for (int i = 0; i < numOfButtons; i++){
                    gButtons[i].setPressed(false);
                }
                break;
            case GAMEOVER:
                for (int i = 0; i < numOfButtons; i++){
                    gButtons[i].setPressed(false);
                }
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
        int temp[] = new int[n];
        for (int i = 0; i < n; i++){
            temp[i] = counter + m + i;
        }
        boolean flag = false;
        for (int i = 0; i < numOfButtons; i++){

            int k = rand.nextInt(n + m);
            while (temp[k] < 0) {
                k = rand.nextInt(n + m);
            }
            gButtons[i].setNumber(temp[k]);
            if (temp[k] == counter){
                flag = true;
            }
            temp[k] = -1;
        }
        if (!flag){
            gButtons[rand.nextInt(numOfButtons)].setNumber(counter);
        }


        for (int i = 0; i < numOfButtons; i++){
            gButtons[i].setBoundsToOriginPosition();
        }
    }
    private void check(GameButton btn) {
        if (isRightAnswer(btn)){
            counter++;
            score++;
            updateButtons(numOfButtons + 1, -1);
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
        updateButtons(numOfButtons, 0);
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
        String s = "";
        switch (difficulty){

            case EASY:
                s = "HIGHSCORE_EASY";
                break;
            case MEDIUM:
                s = "HIGHSCORE_MEDIUM";
                break;
            case HARD:
                s = "HIGHSCORE_HARD";
                break;
        }
        if (pref.getInteger(s, 0) < score) {
            pref.putInteger(s, score);
            pref.flush();
            return true;
        }
        return false;
    }
    private int getHighScore(){
        String s = "";
        switch (difficulty){

            case EASY:
                s = "HIGHSCORE_EASY";
                break;
            case MEDIUM:
                s = "HIGHSCORE_MEDIUM";
                break;
            case HARD:
                s = "HIGHSCORE_HARD";
                break;
        }
        return pref.getInteger(s, 0);
    }

    private void increaseSpeed(){
        for (int i = 0; i < numOfButtons; i++){
            gButtons[i].increaseSpeed();
        }
    }
    private void setSpeedToOrigin(){
        for (int i = 0; i < numOfButtons; i++){
            gButtons[i].setSpeedToOrigin();
        }
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

    private void printText(SpriteBatch sb, float posX, float posY, String text, Color color, BitmapFont font) {
        sb.begin();
        font.setColor(color);
        glyphLayout.setText(font, text);
        font.draw(sb, glyphLayout, posX - glyphLayout.width / 2 + glyphLayout.height / 20, posY + glyphLayout.height / 2);
        sb.end();
    }

    private void changeButtonsColor(){
        myColor = RColor.getColor();
        for (int i = 0; i < numOfButtons; i++){
            gButtons[i].changeColor(myColor);
        }
        btnGameOver.changeColor(myColor);
        btnScore.changeColor(myColor);
        btnHighScore.changeColor(myColor);
    }

}
