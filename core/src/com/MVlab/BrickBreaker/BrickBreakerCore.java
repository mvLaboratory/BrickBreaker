package com.MVlab.BrickBreaker;

import com.MVlab.BrickBreaker.utils.InputHandler;
import com.MVlab.BrickBreaker.gameWorld.GameRenderer;
import com.MVlab.BrickBreaker.gameWorld.GameWorld;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

public class BrickBreakerCore extends ApplicationAdapter {
    private static final String TAG = BrickBreakerCore.class.getName();
    private boolean paused;
    private boolean restart;
    private GameWorld world;
    private GameRenderer renderer;
    private InputHandler inputHandler;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Assets.instance.init(new AssetManager());

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        world = new GameWorld(screenWidth, screenHeight);
        renderer = new GameRenderer(world);

        paused = false;
        inputHandler = new InputHandler(world);

        Gdx.input.setInputProcessor(inputHandler);
    }

    @Override
    public void render() {
        if (!paused && world.active()) {
            Gdx.gl.glClearColor(10 / 255.0f, 15 / 255.0f, 230 / 255.0f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            world.update(Gdx.graphics.getDeltaTime());
            renderer.render();
        }

        if (world.needRestart()) {
            world.init();
            renderer.init();
            Gdx.input.setInputProcessor(new InputHandler(world));

            paused = false;
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
        renderer.dispose();
        Assets.instance.dispose();
    }

    @Override
    public void resume() {
        paused = false;
        Assets.instance.init(new AssetManager());
        renderer.init();
    }
}
