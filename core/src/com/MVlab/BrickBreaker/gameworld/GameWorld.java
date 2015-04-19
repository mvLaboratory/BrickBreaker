/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.gameWorld;
import com.MVlab.BrickBreaker.gameObjects.Border;
import com.MVlab.BrickBreaker.gameObjects.BottomBorder;
import com.MVlab.BrickBreaker.gameObjects.Brick;
import com.MVlab.BrickBreaker.gameObjects.LeftBorder;
import com.MVlab.BrickBreaker.gameObjects.RightBorder;
import com.MVlab.BrickBreaker.utils.Consts;
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
    private LeftBorder leftBorder;
    private RightBorder rightBorder;
    private Border topBorder;
    private BottomBorder bottomBorder;
    private World physicWorld;
    ArrayList<Brick> bricks;
    private float screenWidth;
    private float screenHeight;
    private Boolean stoped;
    private Boolean restart;

    public GameWorld(float screenWidth, float screenHeight) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        init();
    }

    public void init() {
        bricks = new ArrayList<Brick>();

        physicWorld = new World(new Vector2(0, -3F), true);
        physicWorld.setContactListener(this);

        racket = new Racket(0, -2.3f, 1f, 0.2f, 3f, physicWorld);
        ball = new Ball(-1, 0, 0.3f, physicWorld);

        stoped = false;
        restart = false;

        leftBorder = new LeftBorder(Consts.GAME_LEFT_BORDER - 0.01f, -0.5f, 0.15f, Consts.GAME_TOP_BORDER, physicWorld);
        rightBorder = new RightBorder(Consts.GAME_RIGHT_BORDER, -0.5f, 0.15f, Consts.GAME_TOP_BORDER, physicWorld);
        topBorder = new Border(Consts.GAME_RIGHT_BORDER - ((Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2) - 0.01f, Consts.GAME_TOP_BORDER - 0.5f, (Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2, 0.15f, physicWorld);
        bottomBorder = new BottomBorder(Consts.GAME_RIGHT_BORDER - ((Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2) - 0.01f, Consts.GAME_BOTTOM_BORDER - 0.5f, (Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2, 0.01f, physicWorld);

        Vector2 brickPosition = new Vector2(-3.5f, 4);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < Consts.BRICKS_PER_ROW; j++) {
                bricks.add(new Brick(brickPosition.x + j, brickPosition.y + i, 0.4f, 0.25f, physicWorld));
                brickPosition.x += 0.3;
            }
            brickPosition.x = -3.5f;
        }
    }

    public void update(float delta) {
        physicWorld.step(delta, 1, 1);

        for (Brick brick : bricks) {
            if (brick != null) {
                brick.update();
            }
        }
        racket.update(delta);
        ball.update();
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

    public ArrayList<Brick> getBricks() {
        return bricks;
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
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        Object bodyA = a.getUserData();
        Object bodyB = b.getUserData();

        //Brick collides
        if (bodyA instanceof Brick) {
            ((Brick) bodyA).damage(100);
        }

        if (bodyB instanceof Brick) {
            ((Brick) bodyB).damage(100);
        }

        //border collision
        if (bodyA instanceof Ball && bodyB instanceof LeftBorder) {
            ((Ball) bodyA).getPhysicBody().applyLinearImpulse(1, 0, 0, 0, true);
        }

        if (bodyA instanceof LeftBorder && bodyB instanceof Ball) {
            ((Ball) bodyB).getPhysicBody().applyLinearImpulse(1, 0, 0, 0, true);
        }

        if (bodyA instanceof Ball && bodyB instanceof RightBorder) {
            ((Ball) bodyA).getPhysicBody().applyLinearImpulse(-1, 0, 0, 0, true);
        }

        if (bodyA instanceof RightBorder && bodyB instanceof Ball) {
            ((Ball) bodyB).getPhysicBody().applyLinearImpulse(-1, 0, 0, 0, true);
        }

        //Bottom border
        if ((bodyA instanceof Ball && bodyB instanceof BottomBorder) || (bodyA instanceof BottomBorder && bodyB instanceof Ball)) {
            stoped = true;
        }
    }

    public void stop() {
        stoped = true;
    }

    public Boolean active() {
        return !stoped;
    }

    public Boolean needRestart() {
        return restart;
    }

    public void restart() {
        restart = true;
    }
}
