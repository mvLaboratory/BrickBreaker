/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.gameworld;
import com.MVlab.BrickBreaker.gameObjects.Border;
import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.MVlab.BrickBreaker.gameObjects.Ball;
import com.MVlab.BrickBreaker.gameObjects.Racket;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameWorld {
    private Racket racket;
    private Ball ball;
    private Border leftBorder;
    private Border rightBorder;
    private Border topBorder;
    private Border bottomBorder;
    private World physicWorld;
    private float screenWidth;
    private float screenHeight;

    public GameWorld(float screenWidth, float screenHeight) {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        Vector2 screenSize = new Vector2(screenWidth, screenHeight);

        physicWorld = new World(new Vector2(0, -11F), true);

        racket = new Racket(0, -6, 2, 0.5f, physicWorld, screenSize);
        ball = new Ball(0, 0, 0.4f, physicWorld);

        leftBorder = new Border(Consts.GAME_LEFT_BORDER - 0.01f, -0.5f, 0.01f, Consts.GAME_TOP_BORDER, physicWorld);
        rightBorder = new Border(Consts.GAME_RIGHT_BORDER, -0.5f, 0.01f, Consts.GAME_TOP_BORDER, physicWorld);
        topBorder = new Border(Consts.GAME_RIGHT_BORDER - ((Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2) - 0.01f, Consts.GAME_TOP_BORDER - 0.5f, (Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2, 0.01f, physicWorld);
    }

    public void update(float delta) {
        racket.update(delta);
        ball.updatePosition();
    }

    public Racket getRacket() {
        return racket;
    }

    public Ball getBall() {
        return ball;
    }

    public Border getLeftBorder() {
        return leftBorder;
    }

    public Border getRightBorder() {
        return rightBorder;
    }

    public Border getTopBorder() {
        return topBorder;
    }

    public World getPhysicWorld() {
        return physicWorld;
    }
}
