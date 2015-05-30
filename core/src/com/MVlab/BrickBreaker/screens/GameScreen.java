package com.MVlab.BrickBreaker.screens;

import com.MVlab.BrickBreaker.Assets;
import com.MVlab.BrickBreaker.gameWorld.GameRenderer;
import com.MVlab.BrickBreaker.gameWorld.GameWorld;
import com.MVlab.BrickBreaker.utils.InputHandler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by MV on 18.05.2015.
 */
public class GameScreen extends AbstractGameScreen {
    private static final String TAG = GameScreen.class.getName();
    private GameWorld.gameState previousGameState;
    private GameWorld world;
    private GameRenderer renderer;
    private InputHandler inputHandler;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        world = new GameWorld(game);
        renderer = new GameRenderer(world);
        previousGameState = GameWorld.gameState.start;

        inputHandler = new InputHandler(world);
        Gdx.input.setInputProcessor(inputHandler);

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(10 / 255.0f, 15 / 255.0f, 230 / 255.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update(Gdx.graphics.getDeltaTime());
        renderer.render();

        if (world.needRestart()) {
            world.init();
            renderer.init();
            Gdx.input.setInputProcessor(new InputHandler(world));
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void hide() {
        renderer.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        previousGameState = world.getPresentGameState();
        world.setPresentGameState(GameWorld.gameState.paused);
        renderer.dispose();
        Assets.instance.dispose();
    }

    @Override
    public void resume() {
        super.resume();
        world.setPresentGameState(previousGameState);
        renderer.init();
    }
}
