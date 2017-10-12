package com.airse.trickynumbers.models;

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

public class TextButton extends MyButton {

    private BitmapFont font;
    private String text;
    private Color textColor;
    private GlyphLayout glyphLayout;

    public TextButton(String text, MyColor buttonColor, int posX, int posY, int width, int height, BitmapFont font){
        super(buttonColor, posX, posY, width, height);

        this.text = text;
        this.textColor = Color.WHITE;
        this.font = font;
        glyphLayout = new GlyphLayout();
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer shape) {
        if (isPressed){
            drawPressedButtonWithoutText(shape);
            printText(sb, bounds.x + bounds.width / 2, bounds.y + bounds.height / 2 - GAP / 2, 0, text, textColor);
        }
        else{
            drawNotPressedButtonWithoutText(shape);
            printText(sb, bounds.x + bounds.width / 2, bounds.y + bounds.height / 2 + GAP / 2, 0, text, textColor);
        }
    }
    @Override
    public void dispose(){
        font.dispose();
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
}
