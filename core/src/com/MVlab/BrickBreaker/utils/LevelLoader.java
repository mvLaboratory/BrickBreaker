package com.MVlab.BrickBreaker.utils;

import com.MVlab.BrickBreaker.gameWorld.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LevelLoader {
    public static final LevelLoader instance = new LevelLoader();
    public static boolean survival;

    private LevelLoader() { }

    public String[] loadLevel() {
        FileHandle file = Gdx.files.internal("data/levels/lvl" + GameWorld.getLevelNumber() + "1.txt");
        if (!file.exists()) {
            String[] returnString = new String[1];
            returnString[0] = "survival";
            survival = true;

            //file = Gdx.files.internal("data/levels/lvl21.txt");
            return returnString;
        }
        String lvlString = file.readString();
        String[] lvlContent = lvlString.split("");
        return lvlContent;
    }
}
