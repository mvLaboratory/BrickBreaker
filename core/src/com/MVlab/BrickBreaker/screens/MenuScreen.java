package com.MVlab.BrickBreaker.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by MV on 18.05.2015.
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isTouched())
            game.setScreen(new GameScreen(game));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void pause() {
        super.pause();
    }
}

