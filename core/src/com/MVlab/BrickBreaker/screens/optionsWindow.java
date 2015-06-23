package com.MVlab.BrickBreaker.screens;

import com.MVlab.BrickBreaker.utils.AudioManager;
import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class optionsWindow {
    private Stage stage;
    private AbstractGameScreen screen;
    private Skin optionsSkin;

    //options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private CheckBox chkShowFpsCounter;

    public optionsWindow(Stage stage, AbstractGameScreen screen) {
        this.stage = stage;
        this.screen = screen;
    }

    //Game pause
    public void showGameMenu() {
        rebuiltStage();
        loadSettings();
        Gdx.input.setInputProcessor(stage);
        winOptions.setVisible(true);
    }

    public void rebuiltStage() {
        optionsSkin = new Skin(Gdx.files.internal("data/GUI/uiskin.json"), new TextureAtlas("data/GUI/uiskin.atlas"));

        //build layers
        Table layerOptionsWindow = buildOptionsWindowLayer();

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT);

        stack.addActor(layerOptionsWindow);
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

        chkShowFpsCounter.setChecked(prefs.showFpsCounter);
    }

    private void saveSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
        prefs.save();
    }

    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
        AudioManager.instance.onSettingsUpdated();
    }

    private void onCancelClicked() {
        winOptions.setVisible(false);
        AudioManager.instance.onSettingsUpdated();
        Gdx.input.setInputProcessor(screen.getInputProcessor());
        screen.resume();
    }
    //---
}
