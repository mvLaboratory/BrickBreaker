package com.MVlab.BrickBreaker;

import com.badlogic.gdx.Game;
import com.MVlab.BrickBreaker.screens.GameScreen;

public class BrickBreakerCore extends Game {

    @Override
    public void create() {
       // Gdx.app.log("Brick Breaker", "created");
        setScreen(new GameScreen());
    }
}
