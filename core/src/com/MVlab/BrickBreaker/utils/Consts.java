package com.MVlab.BrickBreaker.utils;

/**
 * Created by MV on 01.04.2015.
 */
public class Consts {
    //game consts
    public static float VIEWPORT_WIDTH = 10;
    public static float VIEWPORT_HEIGHT = 15;
    public static float VIEWPORT_GUI_WIDTH = 300;
    public static float VIEWPORT_GUI_HEIGHT = 450;

    public static float GAME_LEFT_BORDER = -5f;
    public static float GAME_RIGHT_BORDER = 3f;
    public static float GAME_CENTER = (GAME_RIGHT_BORDER + GAME_LEFT_BORDER) / 2 + 0.5f;
    public static float GAME_TOP_BORDER = 6.5f;
    public static float GAME_BOTTOM_BORDER = -5.5f;

    public static float BRICKS_PER_ROW = 5f;
    public static int EXTRA_LIFE_CONT = 3;
    public static int DROP_DURATION = 1; // 1 seconds till ball restart
    public static int MID_LEVEL_DURATION = 1; // 1 seconds till level starts
    public static float MID_SCREEN_DURATION = 0.3f; // 1 seconds till screen changes

    public static final String BASIC_TEXTURES_ATLAS_OBJECT = "data/textures/BrickBreackerTextures.pack";

    //Game preferences
    public static final String GAME_OPTIONS = "options.prefs";

    public static int MAX_SURVIVAL_LINES = 15;
    public static int TIME_BETWEEN_SURVIVAL_LINES = 1;
}
