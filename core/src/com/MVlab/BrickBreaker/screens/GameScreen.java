package com.MVlab.BrickBreaker.screens;

import com.MVlab.BrickBreaker.Assets;
import com.MVlab.BrickBreaker.gameWorld.GameRenderer;
import com.MVlab.BrickBreaker.gameWorld.GameWorld;
import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GamePreferences;
import com.MVlab.BrickBreaker.utils.InputHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class GameScreen extends AbstractGameScreen {
    private GameWorld.gameState previousGameState;
    private GameWorld world;
    private GameRenderer renderer;
    private InputHandler inputHandler;

    private Stage stage;
    private optionsWindow optionsWindow;

    public GameScreen(DirectedGame game) {
        super(game);
    }

    //Game pause
    public void showGameMenu() {
        previousGameState = GameWorld.gameState.active;
        optionsWindow = new optionsWindow(stage, this);
        optionsWindow.showGameMenu();
    }

    @Override
    public InputProcessor getInputProcessor () {
        return inputHandler;
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT));
        if (optionsWindow != null) optionsWindow.rebuiltStage();

        GamePreferences.instance.load();
        world = new GameWorld(game);
        renderer = new GameRenderer(world);
        previousGameState = GameWorld.gameState.start;

        inputHandler = new InputHandler(world);
        //Gdx.input.setInputProcessor(inputHandler);

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(10 / 255.0f, 15 / 255.0f, 230 / 255.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        inputHandler.handleInput();
        world.update(Gdx.graphics.getDeltaTime());
        renderer.render();

        if (world.needRestart()) {
            world.init();
            renderer.init();
            inputHandler = new InputHandler(world);
            Gdx.input.setInputProcessor(inputHandler);
        }

        if (world.getPresentGameState() == GameWorld.gameState.paused) {
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        Viewport viewport = stage.getViewport();
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        renderer.dispose();
        Assets.instance.dispose();
        Gdx.input.setCatchBackKey(false);

        stage.dispose();
        if (optionsWindow != null) optionsWindow.dispose();
    }

    @Override
    public void pause() {
        previousGameState = world.getPresentGameState();
        world.setPresentGameState(GameWorld.gameState.paused);
        //renderer.dispose();
        //Assets.instance.dispose();
    }

    @Override
    public void resume() {
        super.resume();
        world.setPresentGameState(previousGameState);
        Gdx.input.setCatchBackKey(true);
       // renderer.init();
    }
}
