package com.MVlab.BrickBreaker.utils;

import com.MVlab.BrickBreaker.gameWorld.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LevelLoader {
    public static final LevelLoader instance = new LevelLoader();

    private LevelLoader() { }

    public String[] loadLevel() {
        FileHandle file = Gdx.files.internal("data/levels/lvl" + GameWorld.getLevelNumber() + "1.txt");
        if (!file.exists()) file = Gdx.files.internal("data/levels/lvl21.txt");
        String lvlString = file.readString();
        String[] lvlContent = lvlString.split("");
        return lvlContent;
    }
}
