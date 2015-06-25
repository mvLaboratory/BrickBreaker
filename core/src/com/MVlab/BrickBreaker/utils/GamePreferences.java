package com.MVlab.BrickBreaker.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {

    public static final GamePreferences instance = new GamePreferences();

    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public boolean showFpsCounter;
    public boolean useAccelerometer;
    public float accelerometerSensitivity;

    private Preferences prefs;

    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Consts.GAME_OPTIONS);
    }

    public void load () {
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0f, 1f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.3f), 0f, 1f);
        //charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0), 0, 2);
        showFpsCounter = prefs.getBoolean("showFpsCounter", false);

        //input settings
        useAccelerometer = prefs.getBoolean("useAccelerometer", false);
        accelerometerSensitivity = MathUtils.clamp(prefs.getFloat("accelerometerSensitivity", 0.3f), 0f, 2f);
    }

    public void save () {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        //prefs.putFloat("charSkin", charSkin);
        prefs.putBoolean("showFpsCounter", showFpsCounter);

        //input settings
        prefs.putBoolean("useAccelerometer", useAccelerometer);
        prefs.putFloat("accelerometerSensitivity", accelerometerSensitivity);

        prefs.flush();
    }
}
