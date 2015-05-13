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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class GameWorld  implements ContactListener {
    public static final String TAG = GameWorld.class.getName();
    private Racket racket;
    private Ball ball;
    private LeftBorder leftBorder;
    private RightBorder rightBorder;
    private Border topBorder;
    private BottomBorder bottomBorder;
    private World physicWorld;
    ArrayList<Brick> bricks;
    private int score;
    private int extraLivesCount;
    private int levelNumber;
    private float gameDuration;
    private float dropDuration;
    private float midLevelDuration;
    private gameState presentGameState;

    public enum gameState {start, restart, levelStart, levelRestart, active, paused, dropped, levelEnd, gameOver, gameRestart};

    public GameWorld() {
        extraLivesCount = Consts.EXTRA_LIFE_CONT;
        bricks = new ArrayList<Brick>();

        presentGameState = gameState.start;

        init();
    }

    public void init() {
        physicWorld = new World(new Vector2(0, -3F), true);
        physicWorld.setContactListener(this);

        racket = new Racket(Consts.GAME_CENTER, -2.3f, 1f, 0.2f, 3f, physicWorld);
        ball = new Ball(Consts.GAME_CENTER, -2.15f, 0.3f, physicWorld);

        leftBorder = new LeftBorder(Consts.GAME_LEFT_BORDER + 0.15f, -0.5f, 0.15f, Consts.GAME_TOP_BORDER, physicWorld);
        rightBorder = new RightBorder(Consts.GAME_RIGHT_BORDER, -0.5f, 0.15f, Consts.GAME_TOP_BORDER, physicWorld);
        topBorder = new Border(Consts.GAME_RIGHT_BORDER - ((Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2) - 0.01f, Consts.GAME_TOP_BORDER - 0.5f, (Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2, 0.15f, physicWorld);
        bottomBorder = new BottomBorder(Consts.GAME_RIGHT_BORDER - ((Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2) - 0.01f, Consts.GAME_BOTTOM_BORDER - 0.5f, (Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2, 0.01f, physicWorld);

        if (presentGameState == gameState.start || presentGameState == gameState.gameRestart){
            score = 0;
            gameDuration = 0;
            levelNumber = 0;
        }
        dropDuration = 0;
        midLevelDuration = 0;

        if (presentGameState == gameState.gameRestart) presentGameState = gameState.start;
        if (presentGameState == gameState.levelRestart) presentGameState = gameState.levelStart;

        initLevel();
    }

    //Levels+++
    public void initLevel() {
        ArrayList<Brick> tempBricks = new ArrayList<Brick>();

        if (levelStart()) {
            loadLevel();
        }
        else {
            for (Brick brick : bricks) {
                if (! brick.existing()) continue;
                tempBricks.add(new Brick(brick.getBrickX(), brick.getBrickY(), brick.getBrickWidth(), brick.getBrickHeight(), physicWorld));
            }
            bricks.clear();
            bricks.addAll(tempBricks);
        }
    }

    public void loadLevel() {
        bricks.clear();
        Vector2 brickPosition = new Vector2(-3.5f, 2.5f);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < Consts.BRICKS_PER_ROW; j++) {
                bricks.add(new Brick(brickPosition.x + j, brickPosition.y + i, 0.4f, 0.25f, physicWorld));
                brickPosition.x += 0.3;
            }
            brickPosition.x = -3.5f;
        }
        levelNumber++;
    }

    public boolean levelStart() {
        return presentGameState == gameState.levelStart || presentGameState == gameState.start;
    }
    //Levels---

    public void update(float delta) {
        if (active()) {
            physicWorld.step(delta, 1, 1);
            gameDuration += delta;

            int activeBricksCount = 0;
            for (Brick brick : bricks) {
                if (brick != null) {
                    brick.update();
                    if (brick.existing()) activeBricksCount++;
                }
            }
            if (activeBricksCount == 0) {
                presentGameState = gameState.levelEnd;
                //initLevel();
            }

            racket.update(delta);
            ball.update();
            if (ball.underBottomBorder()) {
                drop();
            }
        }

        if (presentGameState == gameState.dropped) {
            if (dropDuration > Consts.DROP_DURATION) {
                presentGameState = gameState.restart;
                dropDuration = 0;
            }
            dropDuration += delta;
        }

        if (presentGameState == gameState.levelEnd) {
            if (midLevelDuration > Consts.MID_LEVEL_DURATION) {
                presentGameState = gameState.levelRestart;
                midLevelDuration = 0;
            }
            midLevelDuration += delta;
        }
    }

    public void kickTheBall() {
        int ballDirectionX = MathUtils.random(-1, 2);
        ballDirectionX = ballDirectionX > 0 ? 1 : -1;
        ball.getPhysicBody().applyLinearImpulse(15 * ballDirectionX, 80, 0, 0, true);
        presentGameState = gameState.active;
    }

    public void scored() {
        score += 1;
    }

    public int getScore() {
        return score;
    }

    public void drop() {
        extraLivesCount--;
        if (extraLivesCount <= 0)
            presentGameState = gameState.gameOver;
        else
            presentGameState = gameState.dropped;
    }

    public Boolean active() {
        return presentGameState == gameState.active;
    }

    public Boolean needRestart() {
        return presentGameState == gameState.restart || presentGameState == gameState.levelRestart || presentGameState == gameState.gameRestart;
    }

    public void restart() {
        presentGameState = gameState.restart;
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

    public float getGameDuration() {
        return gameDuration;
    }

    public int getExtraLivesCount() {
        return extraLivesCount;
    }

    public void rebootExtraLivesCount() {
        extraLivesCount = Consts.EXTRA_LIFE_CONT;
    }

    public gameState getPresentGameState() {
        return  presentGameState;
    }

    public void setPresentGameState(gameState presentGameState) {
        this.presentGameState = presentGameState;
    }

    public int getLevelNumber() {
        return levelNumber;
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
            scored();
        }

        if (bodyB instanceof Brick) {
            ((Brick) bodyB).damage(100);
            scored();
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
            drop();
        }
    }

}
