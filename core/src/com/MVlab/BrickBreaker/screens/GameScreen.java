/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.MVlab.BrickBreaker.gameHelpers.InputHandler;
import com.MVlab.BrickBreaker.gameworld.GameRenderer;
import com.MVlab.BrickBreaker.gameworld.GameWorld;

public class GameScreen implements Screen {
    private GameWorld world;
    private GameRenderer renderer;

    public GameScreen() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        world = new GameWorld(screenWidth, screenHeight);
        renderer = new GameRenderer(world);

        Gdx.input.setInputProcessor(new InputHandler(world.getRacket(), world.getBall()));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update(delta);
        renderer.render();
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

    }

    @Override
    public void dispose() {

    }
}
