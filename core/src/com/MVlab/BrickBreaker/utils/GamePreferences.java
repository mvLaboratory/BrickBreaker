package com.MVlab.BrickBreaker.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by MV on 07.06.2015.
 */
public class GamePreferences {
    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();

    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public int charSkin;
    public boolean showFpsCounter;

    private Preferences prefs;

    private GamePreferences() {
        prefs = Gdx.app.getPreferences("");
    }
}
