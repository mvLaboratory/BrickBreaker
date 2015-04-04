package com.MVlab.BrickBreaker.gameHelpers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.MVlab.BrickBreaker.gameObjects.Ball;
import com.MVlab.BrickBreaker.gameObjects.Racket;

/**
 * Created by MV on 17.03.2015.
 */
public class InputHandler implements InputProcessor {
    private Racket racket;
    private Ball ball;

    public InputHandler(Racket racket, Ball ball) {
        this.racket = racket;
        this.ball = ball;
    }

    @Override
    public boolean keyDown(int keycode) {
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
