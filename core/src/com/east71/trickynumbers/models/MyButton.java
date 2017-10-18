package com.east71.trickynumbers.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyButton {

    boolean isPressed;
    int GAP;
    int RADIUS;
    Rectangle bounds;
    MyColor btnColor;
    int w, h;

    MyButton(MyColor buttonColor, float posX, float posY, float width, float height){
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        this.btnColor = buttonColor;
        bounds = new Rectangle(posX, posY, width, height);
        GAP = w / 40;
        RADIUS = 0;
        isPressed = false;
    }
    public MyButton(){
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        this.btnColor = RColor.getColor();
        bounds = new Rectangle(0, 0, w / 2, h / 10);
        GAP = w / 40;
        RADIUS = 0;
        isPressed = false;
    }

    public void update(float dt){
    }
    public void render(SpriteBatch sb, ShapeRenderer shape) {
        if (isPressed){
            drawPressedButtonWithoutText(shape);
        }
        else{
            drawNotPressedButtonWithoutText(shape);
        }
    }
    public void dispose(){
    }

    public Rectangle getBounds() {
        return bounds;
    }
    public boolean contains(Vector2 v){
        return bounds.contains(v);
    }

    public void setPressed(boolean flag){
        isPressed = flag;
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

    public void changeColor(MyColor btnColor){
        this.btnColor = btnColor;
    }
}
