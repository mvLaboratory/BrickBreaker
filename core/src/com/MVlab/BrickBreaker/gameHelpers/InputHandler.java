package com.MVlab.BrickBreaker.gameHelpers;

import com.MVlab.BrickBreaker.gameWorld.GameRenderer;
import com.MVlab.BrickBreaker.gameWorld.GameWorld;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.MVlab.BrickBreaker.gameObjects.Ball;
import com.MVlab.BrickBreaker.gameObjects.Racket;

/**
 * Created by MV on 17.03.2015.
 */
public class InputHandler implements InputProcessor {
    private GameWorld gameWorld;
    private GameRenderer gameRenderer;
    private Racket racket;
    private Ball ball;

    public InputHandler(GameWorld gameWorld, GameRenderer renderer) {
        this.gameWorld = gameWorld;
        this.gameRenderer = renderer;
        this.racket = gameWorld.getRacket();
        this.ball = gameWorld.getBall();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.R) {
            gameWorld.init();
            gameRenderer.init();
        }
        else if(keycode == Input.Keys.LEFT) gameRenderer.moveCamera(-0.5f, 0);
        else if(keycode == Input.Keys.RIGHT) gameRenderer.moveCamera(0.5f, 0);
        else if(keycode == Input.Keys.UP) gameRenderer.moveCamera(0, 0.5f);
        else if(keycode == Input.Keys.DOWN) gameRenderer.moveCamera(0, -0.5f);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        racket.onClick(screenX);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        racket.onDrag(screenX);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
