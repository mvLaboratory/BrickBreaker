/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.gameworld;

import com.MVlab.BrickBreaker.gameObjects.Border;
import com.MVlab.BrickBreaker.gameObjects.Brick;
import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.MVlab.BrickBreaker.gameObjects.Ball;
import com.MVlab.BrickBreaker.gameObjects.Racket;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class GameRenderer {
    GameWorld world;
    World physicWorld;
    Ball ball;
    Racket racket;
    Border leftBorder;
    Border rightBorder;
    Border topBorder;
    ArrayList<Brick> bricks;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
    private Box2DDebugRenderer b2debugRenderer;

    public GameRenderer(GameWorld world) {
        this.b2debugRenderer = new Box2DDebugRenderer(true, false, false, false, false, false);
        this.world = world;
        this.physicWorld = world.getPhysicWorld();
        ball = world.getBall();
        racket = world.getRacket();
        leftBorder = world.getLeftBorder();
        rightBorder = world.getRightBorder();
        topBorder = world.getTopBorder();

        bricks = world.getBricks();

        cam = new OrthographicCamera(Consts.VIEWPORT_WIDTH, Consts.VIEWPORT_HEIGHT);
        cam.position.set(0, 0, 0);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    public void render() {
        cam.update();
        physicWorld.step(Gdx.graphics.getDeltaTime(), 1, 1);

        //deleting bodies+++
        ArrayList<Integer> deleteIndexes = new ArrayList<Integer>();
        int index = -1;
        for (Brick brick : bricks) {
            index++;
            if (brick != null && brick.getHealth() <= 0) {
                brick.getBody().setActive(false);
                physicWorld.destroyBody(brick.getBody());
                deleteIndexes.add(index);
            }
        }
        for (Integer delIndex: deleteIndexes)
            bricks.remove(delIndex.intValue());
        //deleting bodies---

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (DEBUG_DRAW_BOX2D_WORLD) {
            b2debugRenderer.render(physicWorld,
                    cam.combined);
        }

        //borders+++
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
//        shapeRenderer.rect(leftBorder.getX(), leftBorder.getY(), leftBorder.getWidth(), leftBorder.getHeight());
//        shapeRenderer.end();
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
//        shapeRenderer.rect(rightBorder.getX() - 1, rightBorder.getY(), rightBorder.getWidth(), rightBorder.getHeight());
//        shapeRenderer.end();
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
//        shapeRenderer.rect(topBorder.getX(), topBorder.getY(), topBorder.getWidth(), topBorder.getHeight());
//        shapeRenderer.end();
        //borders---

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
//        shapeRenderer.rect(racket.getX(), racket.getY(), racket.getWidth(), racket.getHeight());
//        shapeRenderer.end();
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
//        shapeRenderer.rect(racket.getX(),racket.getY(), racket.getWidth(), racket.getHeight());
//        shapeRenderer.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
//        shapeRenderer.circle(ball.getX(), ball.getY(), ball.getRadius());
//        shapeRenderer.end();
    }
}
