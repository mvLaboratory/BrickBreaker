package com.MVlab.BrickBreaker;

import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
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
    public AssetLevelDecoration levelDecoration;
    public PipeTexture pipe;
    public BorderTexture border;

    private Assets() {};

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Consts.BASIC_TEXTURES_ATLAS_OBJECT, TextureAtlas.class);
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        TextureAtlas atlas = assetManager.get(Consts.BASIC_TEXTURES_ATLAS_OBJECT);
        for (Texture t : atlas.getTextures())
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        racket = new RacketTexture(atlas);
        border = new BorderTexture(atlas);
        pipe = new PipeTexture(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
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

    public class AssetLevelDecoration{
        public final TextureAtlas.AtlasRegion background;

        public AssetLevelDecoration(TextureAtlas atlas) {
            background = atlas.findRegion("background");
        }
    }

    public class PipeTexture{
        public final TextureAtlas.AtlasRegion pipe;

        public PipeTexture(TextureAtlas atlas) {
            pipe = atlas.findRegion("pipe");
        }
    }

    public class BorderTexture{
        public final TextureAtlas.AtlasRegion border;

        public BorderTexture(TextureAtlas atlas) {
            border = atlas.findRegion("border");
        }
    }
}
