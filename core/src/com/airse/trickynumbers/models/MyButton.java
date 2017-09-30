package com.airse.trickynumbers.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by qwert on 24.09.2017.
 */

public class MyButton {

    private int speed;
    private int originSpeed;
    private static final String SHADOW = "eaeaea";
    private static final String BORDER = "c5c4c4";
    private int GAP;
    private int RADIUS;
    private String text;
    private int textSize;
    private Rectangle bounds;
    private Rectangle origin;
    private boolean isPressed;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private Color textColor;
    private MyColor btnColor;
    private int w, h;
    private Vector2 zero;

    public MyButton(String text, int textSize, MyColor buttonColor, int posX, int posY, int width, int height, BitmapFont font){
        this.text = text;
        this.textSize = textSize;
        this.textColor = Color.WHITE;
        this.btnColor = buttonColor;
        bounds = new Rectangle(posX, posY, width, height);
        origin = new Rectangle(posX, posY, width, height);
        this.font = font;

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        GAP = w / 80;
        RADIUS = 2 * GAP;
        isPressed = false;
        glyphLayout = new GlyphLayout();
        originSpeed = w / 4;
        speed = originSpeed;
        zero = new Vector2(0, 0);
    }

    public void update(float dt){
        bounds.x = Math.max(bounds.x - dt * speed, zero.x);
        bounds.width = Math.min(bounds.width + dt * 2 * speed, w);
    }

    public void render(SpriteBatch sb, ShapeRenderer shape) {

        if (isPressed){
            drawPressedButtonWithoutText(shape);
            printText(sb, bounds.x + bounds.width / 2, bounds.y + bounds.height / 2 - GAP, 0, text, textColor);
        }
        else{
            drawNotPressedButtonWithoutText(shape);
            printText(sb, bounds.x + bounds.width / 2, bounds.y + bounds.height / 2 + GAP, 0, text, textColor);
        }

    }

    public void dispose(){
        font.dispose();
    }

    public Rectangle getBounds() {
        return bounds;
    }
    public void setBoundsToOriginPosition() {
        bounds.x = origin.x;
        bounds.y = origin.y;
        bounds.width = origin.width;
        bounds.height = origin.height;
    }

    public boolean contains(Vector2 v){
        return bounds.contains(v);
    }

    public void setPressed(boolean flag){
        isPressed = flag;
    }

    public int getNumber() {
        return Integer.parseInt(text);
    }
    public void setNumber(int number) {
        text = "" + number;
    }

    public void setText(String text) {
        this.text = text;
    }

    private void printText(SpriteBatch sb, float posX, float posY, float angle, String text, Color color) {
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

    private void drawNotPressedButtonWithoutText(ShapeRenderer shape){
        //Shadow
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(btnColor.a700); // Color
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + RADIUS, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + RADIUS, RADIUS);
        shape.rect(bounds.x + RADIUS, bounds.y, bounds.width - RADIUS * 2, RADIUS * 2);
        shape.rect(bounds.x, bounds.y + RADIUS, bounds.width, RADIUS * 2);

        //Button
        shape.setColor(btnColor.a400); // Color
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + RADIUS + RADIUS, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + RADIUS + RADIUS, RADIUS);
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + bounds.height - RADIUS, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + bounds.height - RADIUS, RADIUS);
        shape.rect(bounds.x + RADIUS, bounds.y + RADIUS, bounds.width - RADIUS * 2, bounds.height - RADIUS);
        shape.rect(bounds.x, bounds.y + RADIUS * 2, RADIUS * 2, bounds.height - RADIUS * 3);
        shape.rect(bounds.x + bounds.width - RADIUS * 2, bounds.y + RADIUS * 2, RADIUS * 2, bounds.height - RADIUS * 3);
        shape.end();
    }
    private void drawPressedButtonWithoutText(ShapeRenderer shape){
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(btnColor.a400); // Color
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + RADIUS, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + RADIUS, RADIUS);
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + bounds.height - RADIUS * 2, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + bounds.height - RADIUS * 2, RADIUS);
        shape.rect(bounds.x + RADIUS, bounds.y, bounds.width - RADIUS * 2, bounds.height - RADIUS);
        shape.rect(bounds.x, bounds.y + RADIUS, bounds.width, bounds.height - RADIUS * 3);
        shape.end();
    }

    public boolean check(){
        return bounds.x == 0;
    }

    public void increaseSpeed(){
        speed += originSpeed * 0.1f;
    }
    public void setSpeedToOrigin(){
        speed = originSpeed;
    }

    public void changeColor(MyColor btnColor){
        this.btnColor = btnColor;
    }

}
