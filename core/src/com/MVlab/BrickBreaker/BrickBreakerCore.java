package com.MVlab.BrickBreaker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.MVlab.BrickBreaker.screens.GameScreen;

public class BrickBreakerCore extends Game {

    @Override
    public void create() {
       // Gdx.app.log("Brick Breaker", "created");
        setScreen(new GameScreen());
    }
}
