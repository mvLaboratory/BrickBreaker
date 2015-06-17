package com.MVlab.BrickBreaker;

import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.audio.Sound;

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
    //sounds
    public AssetSounds sounds;
    //music
    public AssetMusic music;

    private Assets() {};

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Consts.BASIC_TEXTURES_ATLAS_OBJECT, TextureAtlas.class);
        //load sounds+++
        assetManager.load("data/sounds/explosion.wav", Sound.class);
        assetManager.load("data/sounds/hit.wav", Sound.class);
        assetManager.load("data/sounds/doors.mp3", Sound.class);
        assetManager.load("data/sounds/drop.wav", Sound.class);
        //---
        // load music
        assetManager.load("data/music/mainTheme.Wav", Music.class);
        assetManager.finishLoading();
        //---

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

        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
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
        public final TextureAtlas.AtlasRegion background3;

        public AssetLevelDecoration(TextureAtlas atlas) {
            background = atlas.findRegion("background");
            background2 = atlas.findRegion("background2");
            background3 = atlas.findRegion("background3");
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
            defaultBig.setScale(2.5f);

            tableNormal.setScale(1f);

            defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            tableNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }

    public class AssetMusic {
        public final Music theme;

        public AssetMusic(AssetManager am) {
            theme = am.get("data/music/mainTheme.Wav", Music.class);
        }
    }

    public class AssetSounds {
        public final Sound explosion;
        public final Sound hit;
        public final Sound doors;
        public final Sound drop;

        public AssetSounds(AssetManager am) {
            this.explosion = am.get("data/sounds/explosion.wav", Sound.class);
            this.hit = am.get("data/sounds/hit.wav", Sound.class);
            this.doors = am.get("data/sounds/doors.mp3", Sound.class);
            this.drop = am.get("data/sounds/drop.wav", Sound.class);
        }
    }
}
