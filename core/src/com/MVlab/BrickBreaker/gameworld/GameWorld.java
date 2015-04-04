/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.gameworld;
import com.MVlab.BrickBreaker.gameObjects.Border;
import com.MVlab.BrickBreaker.gameObjects.Brick;
import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.MVlab.BrickBreaker.gameObjects.Ball;
import com.MVlab.BrickBreaker.gameObjects.Racket;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class GameWorld  implements ContactListener {
    private Racket racket;
    private Ball ball;
    private Border leftBorder;
    private Border rightBorder;
    private Border topBorder;
    private World physicWorld;
    ArrayList<Brick> bricks;
    public ArrayList<Body> deletationBricks;
    private float screenWidth;
    private float screenHeight;

    public GameWorld(float screenWidth, float screenHeight) {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);


        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        Vector2 screenSize = new Vector2(screenWidth, screenHeight);
        bricks = new ArrayList<Brick>();
        deletationBricks = new ArrayList<Body>();

        physicWorld = new World(new Vector2(0, -11F), true);
        physicWorld.setContactListener(this);

        racket = new Racket(0, -6, 2, 0.5f, physicWorld, screenSize);
        ball = new Ball(0, 0, 0.4f, physicWorld);

        leftBorder = new Border(Consts.GAME_LEFT_BORDER - 0.01f, -0.5f, 0.01f, Consts.GAME_TOP_BORDER, physicWorld);
        rightBorder = new Border(Consts.GAME_RIGHT_BORDER, -0.5f, 0.01f, Consts.GAME_TOP_BORDER, physicWorld);
        topBorder = new Border(Consts.GAME_RIGHT_BORDER - ((Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2) - 0.01f, Consts.GAME_TOP_BORDER - 0.5f, (Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2, 0.01f, physicWorld);

        Vector2 brickPosition = new Vector2(-6.3f, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < Consts.BRICKS_PER_ROW; j++) {
                bricks.add(new Brick(brickPosition.x + j, brickPosition.y + i, 0.5f, 0.3f, physicWorld));
                brickPosition.x += 0.3;
            }
            brickPosition.x = -6.3f;
        }
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

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
                Body a=contact.getFixtureA().getBody();
        Body b=contact.getFixtureB().getBody();

        Object bodyA = a.getUserData();
        Object bodyB = b.getUserData();
        Body deleteBody = null;

        if (bodyA instanceof Brick) {
            deleteBody = a;
        }

        if (bodyB instanceof Brick) {
            deleteBody = b;
        }

        if  (deleteBody != null) {
            deletationBricks.add(deleteBody);
        }
    }
}
