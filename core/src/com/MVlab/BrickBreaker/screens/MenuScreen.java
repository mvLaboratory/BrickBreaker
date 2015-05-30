package com.MVlab.BrickBreaker.screens;

import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by MV on 18.05.2015.
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    private Stage stage;
    private Skin skinBrickBreaker;

    //menu
    private Button btnMenuQuickPaly;
    private Button btnMenuQuit;

    public MenuScreen(Game game) {
        super(game);
    }

    public void rebuiltStage() {
        skinBrickBreaker = new Skin(Gdx.files.internal("data/GUI/game_ui.json"), new TextureAtlas("data/GUI/BrickBreackerButtons.pack"));

        //build layers
        Table layerControls = buildControlsLayer();

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT);
        stack.add(layerControls);
    }

    private Table buildControlsLayer() {
        Table layer = new Table();
        layer.center().center();
        //quick play
        btnMenuQuickPaly = new Button(skinBrickBreaker, "qPlay");
        layer.add(btnMenuQuickPaly);
        btnMenuQuickPaly.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });
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

        stage.act(delta);
        stage.draw();

//        if (Gdx.input.isTouched())
//            game.setScreen(new GameScreen(game));
    }

    @Override
    public void resize(int width, int height) {
        Viewport viewport = stage.getViewport();
        //viewport.setScreenHeight(Consts.VIEWPORT_GUI_HEIGHT);
        viewport.setWorldHeight(Consts.VIEWPORT_GUI_HEIGHT);
        viewport.setScreenWidth(width);
        viewport.setWorldWidth(Consts.VIEWPORT_GUI_WIDTH);
        stage.setViewport(viewport);
                //Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT, false);
    }

    @Override
    public void show() {
        stage = new Stage();
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

