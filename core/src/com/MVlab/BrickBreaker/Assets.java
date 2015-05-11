package com.MVlab.BrickBreaker;

import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by MV on 09.04.2015.
 */
public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;

    public RacketTexture racket;
    public BallTexture ball;
    public AssetLevelDecoration levelDecoration;
    public PipeTexture pipe;
    public BorderTexture border;
    public TopBorderTexture topBorder;
    public BrickTexture brickTexture;
    public AssetFonts fonts;

    private Assets() {};

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Consts.BASIC_TEXTURES_ATLAS_OBJECT, TextureAtlas.class);
        assetManager.finishLoading();

//        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
//        for (String a : assetManager.getAssetNames())
//            Gdx.app.debug(TAG, "asset: " + a);

        TextureAtlas atlas = assetManager.get(Consts.BASIC_TEXTURES_ATLAS_OBJECT);
        for (Texture t : atlas.getTextures())
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        racket = new RacketTexture(atlas);
        ball = new BallTexture(atlas);
        border = new BorderTexture(atlas);
        topBorder = new TopBorderTexture(atlas);
        pipe = new PipeTexture(atlas);
        brickTexture = new BrickTexture(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);

        fonts = new AssetFonts();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();

        fonts.tableNormal.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
    }

    public class RacketTexture{
        public final TextureAtlas.AtlasRegion racket;

        public RacketTexture(TextureAtlas atlas) {
            racket = atlas.findRegion("Racket");
        }
    }

    public class BorderTexture{
        public final TextureAtlas.AtlasRegion border;

        public BorderTexture(TextureAtlas atlas) {
            border = atlas.findRegion("border");
        }
    }

    public class TopBorderTexture{
        public final TextureAtlas.AtlasRegion topBorder;

        public TopBorderTexture(TextureAtlas atlas) {
            topBorder = atlas.findRegion("topBorder");
        }
    }

    public class PipeTexture{
        public final TextureAtlas.AtlasRegion pipe;

        public PipeTexture(TextureAtlas atlas) {
            pipe = atlas.findRegion("pipe");
        }
    }

    public class BallTexture{
        public final TextureAtlas.AtlasRegion ball;

        public BallTexture(TextureAtlas atlas) {
            ball = atlas.findRegion("ball");
        }
    }

    public class BrickTexture{
        public final TextureAtlas.AtlasRegion brick;

        public BrickTexture(TextureAtlas atlas) {
            brick = atlas.findRegion("brick");
        }
    }

    public class AssetLevelDecoration{
        public final TextureAtlas.AtlasRegion background;
        public final TextureAtlas.AtlasRegion background2;

        public AssetLevelDecoration(TextureAtlas atlas) {
            background = atlas.findRegion("background");
            background2 = atlas.findRegion("background2");
        }
    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public final BitmapFont tableNormal;

        public AssetFonts() {
            defaultSmall = new BitmapFont(Gdx.files.internal("data/fonts/arial-15.fnt"), false);
            defaultNormal = new BitmapFont(Gdx.files.internal("data/fonts/arial-15.fnt"), false);
            defaultBig = new BitmapFont(Gdx.files.internal("data/fonts/arial-15.fnt"), false);

            tableNormal = new BitmapFont(Gdx.files.internal("data/fonts/tableFont1.fnt"), false);

            defaultSmall.setScale(0.75f);
            defaultNormal.setScale(1f);
            defaultBig.setScale(3f);

            tableNormal.setScale(1f);

            defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            tableNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }
}
