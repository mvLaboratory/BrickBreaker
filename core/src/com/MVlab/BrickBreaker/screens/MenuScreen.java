package com.MVlab.BrickBreaker.screens;

import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GamePreferences;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
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
    private Skin OptionsSkin;

    //menu
    private Image imgBackground;
    private Image imgRacket;
    private Button btnMenuQuickPlay;
    private Button btnMenuQuit;
    private Button btnOptions;
    private Button btnMenuPlay;
    private Button btnMenuOptions;

    //options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private SelectBox selCharSkin;
    private Image imgCharSkin;
    private CheckBox chkShowFpsCounter;

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

    private void loadSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        chkSound.setChecked(prefs.sound);
        sldSound.setValue(prefs.volSound);
        chkMusic.setChecked(prefs.music);
        sldMusic.setValue(prefs.volMusic);
        //selCharSkin.setSelection(prefs.charSkin);
        //onCharSkinSelected(prefs.charSkin);
        chkShowFpsCounter.setChecked(prefs.showFpsCounter);
    }

    private void saveSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        //prefs.charSkin = selCharSkin.getSelectionIndex();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
        prefs.save();
    }

//    private void onCharSkinSelected(int index) {
//        CharacterSkin skin = CharacterSkin.values()[index];
//        imgCharSkin.setColor(skin.getColor());
//    }

    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
    }
    private void onCancelClicked() {
        btnMenuPlay.setVisible(true);
        btnMenuOptions.setVisible(true);
        winOptions.setVisible(false);
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
        stage = new Stage(new StretchViewport(Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT));
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

