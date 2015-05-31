package com.MVlab.BrickBreaker.screens;

import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by MV on 18.05.2015.
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    private Stage stage;
    private Skin skinBrickBreaker;

    //menu
    private Image imgBackground;
    private Image imgRacket;
    private Button btnMenuQuickPlay;
    private Button btnMenuQuit;
    private Button btnOptions;

    // debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;

    public MenuScreen(Game game) {
        super(game);
    }

    public void rebuiltStage() {
        skinBrickBreaker = new Skin(Gdx.files.internal("data/GUI/game_ui.json"), new TextureAtlas("data/GUI/BrickBreackerGUI.pack"));

        //build layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerControls = buildControlsLayer();

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT);

        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerControls);
    }

    private Table buildBackgroundLayer() {
        Table layer = new Table();
        imgBackground = new Image(skinBrickBreaker, "backGround");
        layer.left().bottom();
        layer.add(imgBackground);
        return layer;
    }

    private Table buildObjectsLayer() {
        Table layer = new Table();
        imgRacket = new Image(skinBrickBreaker, "Racket");
        layer.left().bottom();
        layer.addActor(imgRacket);
        return layer;
    }

    private Table buildControlsLayer() {
        Table layer = new Table();
        layer.center().center();
        //quick play
        btnMenuQuickPlay = new Button(skinBrickBreaker, "qPlay");
        layer.add(btnMenuQuickPlay);
        btnMenuQuickPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });
        layer.row();
        layer.row();

        //options
        btnOptions = new Button(skinBrickBreaker, "Options");
        layer.add(btnOptions);
        btnOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });
        layer.row();
        layer.row();

        //quit
        btnMenuQuit = new Button(skinBrickBreaker, "quit");
        layer.add(btnMenuQuit);
        btnMenuQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        return layer;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (debugEnabled) {
            debugRebuildStage -= delta;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuiltStage();
            }
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        Viewport viewport = stage.getViewport();
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT * 0.8f));
        Gdx.input.setInputProcessor(stage);
        rebuiltStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinBrickBreaker.dispose();
    }

    @Override
    public void pause() {
        super.pause();
    }
}

