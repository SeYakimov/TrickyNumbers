package com.east71.trickynumbers.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.east71.trickynumbers.IActivityRequestHandler;

public class LoadingState extends State {

    private AssetManager manager;
    private int h, w, logoH, logoW;
    private float percent;
    private ShapeRenderer shape;
    private Texture logo;
    private boolean logoShowed;
    private IActivityRequestHandler handler;

    public LoadingState(AssetManager manager, GameStateManager gsm, IActivityRequestHandler handler) {
        super(gsm);
        this.manager = manager;
        this.handler = handler;
        handler.showAds(false);

        percent = 0;
        shape = new ShapeRenderer();
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        logoShowed = false;

        manager.load("drawables/BGDark.png", Texture.class);
        manager.load("drawables/BGWhite.png", Texture.class);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        int textSize = (int)(w * 0.07f);
        FreetypeFontLoader.FreeTypeFontLoaderParameter size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size2Params.fontFileName = "fonts/FFFForward.ttf";
        size2Params.fontParameters.size = textSize;
        size2Params.fontParameters.incremental = true;
        manager.load("small.ttf", BitmapFont.class, size2Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size1Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size1Params.fontFileName = "fonts/FFFForward.ttf";
        textSize = (int) (w * 0.16f);
        size1Params.fontParameters.size = textSize;
        size1Params.fontParameters.incremental = true;
        manager.load("normal.ttf", BitmapFont.class, size1Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size3Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size3Params.fontFileName = "fonts/FFFForward.ttf";
        textSize = (int)(w * 0.24f);
        size3Params.fontParameters.size = textSize;
        size3Params.fontParameters.incremental = true;
        manager.load("big.ttf", BitmapFont.class, size3Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size4Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size4Params.fontFileName = "fonts/FFFForward.ttf";
        textSize = (int)(w * 0.125f);
        size4Params.fontParameters.size = textSize;
        size4Params.fontParameters.incremental = true;
        manager.load("gameName.ttf", BitmapFont.class, size4Params);

        logo = new Texture("drawables/EAST71.png");
        logoH = (int)(w * 0.8) * logo.getHeight() / logo.getWidth();
        logoW = (int)(w * 0.8);
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
        if (manager.update() && logoShowed ) {
            gsm.push(new MenuState(gsm, manager, handler));
        }
        percent = Interpolation.linear.apply(percent, manager.getProgress(), 0.1f);
        if (percent > 0.99f) {
            logoShowed = true;
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(logo, w / 10, h / 2 - logoH / 2, logoW, logoH);
        sb.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.WHITE);
        shape.rect(w / 10, (h - logoH) / 2 - (int)(logoH / 5 * 1.5f), percent * logoW, logoH / 5);
        shape.end();
    }
    @Override
    public void dispose() {
        shape.dispose();
        logo.dispose();
    }
}
