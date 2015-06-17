package com.MVlab.BrickBreaker;

import com.MVlab.BrickBreaker.screens.DirectedGame;
import com.MVlab.BrickBreaker.screens.MenuScreen;
import com.MVlab.BrickBreaker.screens.transitions.ScreenTransitionSlide;
import com.MVlab.BrickBreaker.screens.transitions.Transitions;
import com.MVlab.BrickBreaker.utils.AudioManager;
import com.MVlab.BrickBreaker.utils.GamePreferences;
import com.MVlab.BrickBreaker.utils.InputHandler;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;

public class BrickBreakerCore extends DirectedGame {
    private InputHandler inputHandler;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Assets.instance.init(new AssetManager());

        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.theme, 0.10f);

        Transitions.ScreenTransition transition = ScreenTransitionSlide.init(0.50f,
                ScreenTransitionSlide.DOWN, true, Interpolation.pow5);
        setScreen(new MenuScreen(this));
    }
}
//public class BrickBreakerCore extends ApplicationAdapter {
//    private static final String TAG = BrickBreakerCore.class.getName();
//    private GameWorld.gameState previousGameState;
//    private GameWorld world;
//    private GameRenderer renderer;
//    private InputHandler inputHandler;
//
//    @Override
//    public void create() {
//        Gdx.app.setLogLevel(Application.LOG_DEBUG);
//
//        Assets.instance.init(new AssetManager());
//        previousGameState = GameWorld.gameState.start;
//
//        world = new GameWorld();
//        renderer = new GameRenderer(world);
//
//        inputHandler = new InputHandler(world);
//        Gdx.input.setInputProcessor(inputHandler);
//    }
//
//    @Override
//    public void render() {
//        Gdx.gl.glClearColor(10 / 255.0f, 15 / 255.0f, 230 / 255.0f, 1f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        world.update(Gdx.graphics.getDeltaTime());
//        renderer.render();
//
//        if (world.needRestart()) {
//            world.init();
//            renderer.init();
//            Gdx.input.setInputProcessor(new InputHandler(world));
//        }
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        renderer.resize(width, height);
//    }
//
//    @Override
//    public void dispose() {
//        renderer.dispose();
//    }
//
//    @Override
//    public void pause() {
//        previousGameState = world.getPresentGameState();
// //       world.setPresentGameState(GameWorld.gameState.paused);
//        renderer.dispose();
//        Assets.instance.dispose();
//    }
//
//    @Override
//    public void resume() {
////        world.setPresentGameState(previousGameState);
//        Assets.instance.init(new AssetManager());
//        renderer.init();
//    }
//}
