package com.airse.trickynumbers.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ImageButton extends MyButton {

    private Texture t;

    public ImageButton(MyColor buttonColor, float posX, float posY, float width, float height, Texture t){
        super(buttonColor, posX, posY, width, height);
        this.t = t;
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer shape) {
        if (isPressed){
            drawPressedButtonWithoutText(shape);
        }
        else{
            drawNotPressedButtonWithoutText(shape);
        }

        drawTexture(sb, t);
    }

    @Override
    public void dispose(){
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

    private void drawTexture(SpriteBatch sb, Texture t){
        float texX;
        float texY;
        if (isPressed){
            texX = bounds.x + bounds.width / 2 - bounds.height / 2 + GAP * 1.5f;
            texY = bounds.y + GAP;

        }
        else{
            texX = bounds.x + bounds.width / 2 - bounds.height / 2 + GAP * 1.5f;
            texY = bounds.y + GAP * 2;
        }
        sb.begin();
        sb.draw(t, texX, texY, bounds.height - GAP * 3, bounds.height - GAP * 3);
        sb.end();
    }
    public void setTexture(Texture t){
        this.t = t;
    }

}
