package com.MVlab.BrickBreaker.utils;

import com.MVlab.BrickBreaker.gameworld.GameRenderer;
import com.MVlab.BrickBreaker.gameworld.GameWorld;
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

    public InputHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.racket = gameWorld.getRacket();
        this.ball = gameWorld.getBall();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.R) {
          gameWorld.restart();
        }
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
        if (gameWorld.active())
            racket.onClick(screenX);

        if (gameWorld.levelStart() || gameWorld.getPresentGameState() == GameWorld.gameState.restart)
            gameWorld.kickTheBall();

        if (gameWorld.getPresentGameState() == GameWorld.gameState.gameOver) {
            gameWorld.rebootExtraLivesCount();
            gameWorld.setPresentGameState(GameWorld.gameState.gameRestart);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (gameWorld.active())
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
