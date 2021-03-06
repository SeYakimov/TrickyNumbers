package com.east71.trickynumbers.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameButton extends MyButton {

    private int speed;
    private int originSpeed;
    private Rectangle origin;
    private Vector2 zero;

    private BitmapFont font;
    private String text;
    private Color textColor;
    private GlyphLayout glyphLayout;

    public GameButton(String text, MyColor buttonColor, float posX, float posY, float width, float height, BitmapFont font){
        super(buttonColor, posX, posY, width, height);

        this.text = text;
        this.textColor = Color.WHITE;
        this.font = font;
        glyphLayout = new GlyphLayout();

        originSpeed = w / 4;
        speed = originSpeed;
        zero = new Vector2(0, 0);
        origin = new Rectangle(posX, posY, width, height);
    }

    @Override
    public void update(float dt){
        bounds.x = Math.max(bounds.x - dt * speed, zero.x);
        bounds.width = Math.min(bounds.width + dt * 2 * speed, w);
    }
    @Override
    public void render(SpriteBatch sb, ShapeRenderer shape) {
        if (isPressed){
            drawPressedButtonWithoutText(shape);
            printText(sb, bounds.x + bounds.width / 2, bounds.y + bounds.height / 2 - GAP / 2, text, textColor);
        }
        else{
            drawNotPressedButtonWithoutText(shape);
            printText(sb, bounds.x + bounds.width / 2, bounds.y + bounds.height / 2 + GAP / 2, text, textColor);
        }
    }
    @Override
    public void dispose(){
        super.dispose();
        font.dispose();
    }

    public void setBoundsToOriginPosition() {
        bounds.x = origin.x;
        bounds.y = origin.y;
        bounds.width = origin.width;
        bounds.height = origin.height;
    }

    public void reduceBoundsSize(float x){
        bounds.x = Math.min(bounds.x + x, origin.x);
        bounds.width = Math.max(bounds.width - 2 * x, origin.width);
    }

    public int getNumber() {
        return Integer.parseInt(text);
    }
    public void setNumber(int number) {
        text = "" + number;
    }

    private void printText(SpriteBatch sb, float posX, float posY, String text, Color color) {
        sb.begin();
        font.setColor(color);
        glyphLayout.setText(font, text);
        font.draw(sb, glyphLayout, posX - glyphLayout.width / 2 + glyphLayout.height / 20, posY + glyphLayout.height / 2);
        sb.end();
    }

    private void drawNotPressedButtonWithoutText(ShapeRenderer shape){
        //Shadow
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(btnColor.dark); // Color
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + RADIUS, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + RADIUS, RADIUS);
        shape.rect(bounds.x + RADIUS, bounds.y, bounds.width - RADIUS * 2, GAP);
        shape.rect(bounds.x, bounds.y + RADIUS, bounds.width, Math.max(RADIUS, GAP));

        //Button
        shape.setColor(btnColor.normal); // Color
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + GAP + RADIUS, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + GAP + RADIUS, RADIUS);
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + bounds.height - RADIUS, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + bounds.height - RADIUS, RADIUS);

        shape.rect(bounds.x + RADIUS, bounds.y + GAP, bounds.width - RADIUS * 2, bounds.height - GAP);
        shape.rect(bounds.x, bounds.y + RADIUS + GAP, bounds.width, bounds.height - RADIUS * 2 - GAP);
        shape.end();
    }
    private void drawPressedButtonWithoutText(ShapeRenderer shape){
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(btnColor.light); // Color
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + RADIUS, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + RADIUS, RADIUS);
        shape.circle(bounds.x + bounds.width - RADIUS, bounds.y + bounds.height - RADIUS - GAP, RADIUS);
        shape.circle(bounds.x + RADIUS, bounds.y + bounds.height - RADIUS - GAP, RADIUS);

        shape.rect(bounds.x + RADIUS, bounds.y, bounds.width - RADIUS * 2, bounds.height - GAP);
        shape.rect(bounds.x, bounds.y + RADIUS, bounds.width, bounds.height - RADIUS * 2 - GAP);
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
}
