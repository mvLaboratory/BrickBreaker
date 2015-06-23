package com.MVlab.BrickBreaker.screens;

import com.MVlab.BrickBreaker.Assets;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public abstract class AbstractGameScreen implements Screen {
    //protected Game game;
    protected DirectedGame game;


    public AbstractGameScreen (DirectedGame game) {
        this.game = game;
    }

    public abstract InputProcessor getInputProcessor ();

    public abstract void showGameMenu();

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        //Assets.instance.init(new AssetManager());
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }
}
