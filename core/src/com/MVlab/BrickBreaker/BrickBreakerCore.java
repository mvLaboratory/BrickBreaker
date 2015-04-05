package com.MVlab.BrickBreaker;

import com.MVlab.BrickBreaker.gameHelpers.InputHandler;
import com.MVlab.BrickBreaker.gameworld.GameRenderer;
import com.MVlab.BrickBreaker.gameworld.GameWorld;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class BrickBreakerCore extends ApplicationAdapter {
    private static final String TAG = BrickBreakerCore.class.getName();
    private boolean paused;
    private GameWorld world;
    private GameRenderer renderer;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        world = new GameWorld(screenWidth, screenHeight);
        renderer = new GameRenderer(world);

        paused = false;

        Gdx.input.setInputProcessor(new InputHandler(world, renderer));
    }

    @Override
    public void render() {
        if (!paused) {
            Gdx.gl.glClearColor(10 / 255.0f, 15 / 255.0f, 230 / 255.0f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            world.update(Gdx.graphics.getDeltaTime());
            renderer.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }
}
