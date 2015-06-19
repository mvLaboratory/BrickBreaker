package com.MVlab.BrickBreaker.utils;

import com.MVlab.BrickBreaker.gameWorld.GameRenderer;
import com.MVlab.BrickBreaker.gameWorld.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.MVlab.BrickBreaker.gameObjects.Ball;
import com.MVlab.BrickBreaker.gameObjects.Racket;

/**
 * Created by MV on 17.03.2015.
 */
public class InputHandler implements InputProcessor {
    private static final String TAG = InputHandler.class.getName();
    private GameWorld gameWorld;
    private GameRenderer gameRenderer;
    private Racket racket;
    private Ball ball;
    private boolean accelerometerAvailable;

    // Angle of rotation for dead zone (no movement)
    public static final float ACCEL_ANGLE_DEAD_ZONE = 1.0f;
    // Max angle of rotation needed to gain max movement velocity
    public static final float ACCEL_MAX_ANGLE_MAX_MOVEMENT = 20.0f;

    public InputHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.racket = gameWorld.getRacket();
        this.ball = gameWorld.getBall();
        accelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
    }

    public void handleInput() {
        if (accelerometerAvailable) {
            // normalize accelerometer values from [-10, 10] to [-1, 1]
            // which translate to rotations of [-90, 90] degrees
            float amountX = Gdx.input.getAccelerometerX() / 10.0f;
            amountX *= 90.0f;
            // is angle of rotation inside dead zone?
            if (Math.abs(amountX) < ACCEL_ANGLE_DEAD_ZONE) {
                amountX = 0;
            } else {
                // use the defined max angle of rotation instead of
                // the full 90 degrees for maximum velocity
                amountX /= ACCEL_MAX_ANGLE_MAX_MOVEMENT;
            }
            gameWorld.debugAccelerometerMassage = "" + amountX;

            racket.setRocketSpeed(amountX * -2);
        }
        else {
            gameWorld.debugAccelerometerMassage = "N/A";
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.R) {
          gameWorld.restart();
        }

        if (keycode == Input.Keys.BACKSPACE || keycode == Input.Keys.BACK) {
            gameWorld.backToMenu();
        }

        if (keycode == Input.Keys.LEFT)
            racket.setRocketSpeed(-0.1f);
        if (keycode == Input.Keys.RIGHT)
            racket.setRocketSpeed(0.1f);
        return true;
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
