package com.MVlab.BrickBreaker.screens;

import com.MVlab.BrickBreaker.screens.transitions.ScreenTransitionSlide;
import com.MVlab.BrickBreaker.screens.transitions.Transitions;
import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GamePreferences;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

/**
 * Created by MV on 18.05.2015.
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    private Stage stage;
    private Skin skinBrickBreaker;
    private Skin optionsSkin;

    //menu
    private Image imgBackground;
    private Image imgRacket;
    private Button btnMenuQuickPlay;
    private Button btnMenuQuit;
//    private Button btnOptions;
//    private Button btnMenuPlay;
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

    public MenuScreen(DirectedGame game) {
        super(game);
    }

    public void rebuiltStage() {
        skinBrickBreaker = new Skin(Gdx.files.internal("data/GUI/game_ui.json"), new TextureAtlas("data/GUI/BrickBreackerGUI.pack"));
        optionsSkin = new Skin(Gdx.files.internal("data/GUI/uiskin.json"), new TextureAtlas("data/GUI/uiskin.atlas"));

        //build layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT);

        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerControls);
        stack.addActor(layerOptionsWindow);
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
                Transitions.ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                        ScreenTransitionSlide.LEFT, false, Interpolation.pow5);
                game.setScreen(new GameScreen(game), transition);
            }
        });
        layer.row();
        layer.row();

        //options
        btnMenuOptions = new Button(skinBrickBreaker, "Options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
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

    //options+++
    private void onOptionsClicked() {
        loadSettings();
        btnMenuQuickPlay.setVisible(false);
        btnMenuOptions.setVisible(false);
        btnMenuQuit.setVisible(false);
        winOptions.setVisible(true);
    }

    private Table buildOptionsWindowLayer () {
        winOptions = new Window("Options", optionsSkin);
        winOptions.pack();

        // + Audio Settings: Sound/Music CheckBox and Volume Slider
        winOptions.add(buildOptWinAudioSettings()).row();
        // + Character Skin: Selection Box (White, Gray, Brown)
        //  winOptions.add(buildOptWinSkinSelection()).row();
        // + Debug: Show FPS Counter
        winOptions.add(buildOptWinDebug()).row();
        // + Separator and Buttons (Save, Cancel)
        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

        // Make options window slightly transparent
        winOptions.setColor(1, 1, 1, 0.8f);
        // Hide options window by default
        winOptions.setVisible(false);
        if (debugEnabled) winOptions.debug();
        // Let TableLayout recalculate widget sizes and positions
        winOptions.pack();
        // Move options window to bottom right corner
        //winOptions.setPosition(Consts.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
        winOptions.setResizable(true);

        return winOptions;
    }

    private Table buildOptWinAudioSettings () {
        Table tbl = new Table();
        // + Title: "Audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", optionsSkin, "default-font", Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Sound" label, sound volume slider
        chkSound = new CheckBox("", optionsSkin);
        tbl.add(chkSound);
        tbl.add(new Label("Sound", optionsSkin));
        sldSound = new Slider(0.0f, 1.0f, 0.1f, false, optionsSkin);
        tbl.add(sldSound);
        tbl.row();
        // + Checkbox, "Music" label, music volume slider
        chkMusic = new CheckBox("", optionsSkin);
        tbl.add(chkMusic);
        tbl.add(new Label("Music", optionsSkin));
        sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, optionsSkin);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinDebug () {
        Table tbl = new Table();
        // + Title: "Debug"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", optionsSkin, "default-font", Color.RED)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Show FPS Counter" label
        chkShowFpsCounter = new CheckBox("", optionsSkin);
        tbl.add(new Label("Show FPS Counter", optionsSkin));
        tbl.add(chkShowFpsCounter);
        tbl.row();

        return tbl;
    }

    private Table buildOptWinButtons () {
        Table tbl = new Table();
        // + Separator
        Label lbl = null;
        lbl = new Label("", optionsSkin);
        lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = optionsSkin.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
        tbl.row();
        lbl = new Label("", optionsSkin);
        lbl.setColor(0.5f, 0.5f, 0.5f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = optionsSkin.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();
        // + Save Button with event handler
        btnWinOptSave = new TextButton("Save", optionsSkin);
        tbl.add(btnWinOptSave).padRight(30);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
        // + Cancel Button with event handler
        btnWinOptCancel = new TextButton("Cancel", optionsSkin);
        tbl.add(btnWinOptCancel);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onCancelClicked();
            }
        });
        return tbl;
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
        btnMenuQuickPlay.setVisible(true);
        btnMenuOptions.setVisible(true);
        btnMenuQuit.setVisible(true);
        winOptions.setVisible(false);
    }

    private void showOptionsWindow (boolean visible, boolean animated) {
        float alphaTo = visible ? 0.8f : 0.0f;
        float duration = animated ? 1.0f : 0.0f;
        Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
        winOptions.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
    }
    //options---

    @Override
    public InputProcessor getInputProcessor () {
        return stage;
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
        //Gdx.input.setInputProcessor(stage);
        rebuiltStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinBrickBreaker.dispose();
        optionsSkin.dispose();
    }

    @Override
    public void pause() {
        super.pause();
    }
}

