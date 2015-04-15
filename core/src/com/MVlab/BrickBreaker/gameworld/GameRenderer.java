/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.gameworld;

import com.MVlab.BrickBreaker.Assets;
import com.MVlab.BrickBreaker.gameObjects.Border;
import com.MVlab.BrickBreaker.gameObjects.Brick;
import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.MVlab.BrickBreaker.gameObjects.Ball;
import com.MVlab.BrickBreaker.gameObjects.Racket;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public class GameRenderer  implements Disposable {
    GameWorld world;
    World physicWorld;
    Ball ball;
    Racket racket;
    Border leftBorder;
    Border rightBorder;
    Border topBorder;
    ArrayList<Brick> bricks;
    Sprite spr, spr2, sprPipe, sprRacket, sprBall, sprLeftBorder, sprTopBorder, sprBrick;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
    private Box2DDebugRenderer b2debugRenderer;

    public GameRenderer(GameWorld world) {
        this.world = world;
        init();
    }

    public void init() {
        this.b2debugRenderer = new Box2DDebugRenderer(true, false, false, false, false, false);
        this.physicWorld = world.getPhysicWorld();

        cam = new OrthographicCamera(Consts.VIEWPORT_WIDTH, Consts.VIEWPORT_HEIGHT);
        cam.position.set(0, 0, 0);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        initObjects();
    }

    private void initObjects() {
        ball = world.getBall();
        racket = world.getRacket();
        leftBorder = world.getLeftBorder();
        rightBorder = world.getRightBorder();
        topBorder = world.getTopBorder();

        bricks = world.getBricks();

        TextureRegion borderTexture = Assets.instance.border.border;
        borderTexture.flip(true, false);
        sprLeftBorder = new Sprite(borderTexture);

        TextureRegion topBorderTexture = Assets.instance.topBorder.topBorder;
//        borderTopTexture.flip(true, false);
        sprTopBorder = new Sprite(topBorderTexture);

        TextureRegion ballTexture = Assets.instance.ball.ball;
        sprBall = new Sprite(ballTexture);

        TextureRegion racketTexture = Assets.instance.racket.racket;
        sprRacket = new Sprite(racketTexture);

        TextureRegion pipeTexture = Assets.instance.pipe.pipe;
        pipeTexture.flip(false, true);
        sprPipe = new Sprite(pipeTexture);

        TextureRegion regions = Assets.instance.levelDecoration.background;
        spr = new Sprite(regions);

        TextureRegion brickTexture = Assets.instance.brickTexture.brick;
        sprBrick = new Sprite(brickTexture);
    }

    public void render() {
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spr.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        spr.draw(batch);
        batch.end();

        sprPipe.setSize(300, 5);
        sprPipe.setPosition(0, 15);

        batch.begin();
        sprPipe.draw(batch);
        batch.end();

        sprRacket.setSize(racket.getWidth(), racket.getFullHeight());
        sprRacket.setOrigin(25, 50);
        sprRacket.setPosition(racket.getX(), racket.getY());

        batch.begin();
        sprRacket.draw(batch);
        batch.end();


        sprBall.setSize(10, 10);
        sprBall.setPosition(ball.getX(), ball.getY());

        batch.begin();
        sprBall.draw(batch);
        batch.end();

        //bricks
        for (Brick brick : bricks) {
            if (!brick.existing()) continue;
            sprBrick.setSize(brick.getWidth(), brick.getHeight());
            sprBrick.setPosition(brick.getX(), brick.getY());

            batch.begin();
            sprBrick.draw(batch);
            batch.end();
        }
//        sprBrick.setSize(20, 10);
//        sprBrick.setPosition(20, 20);
//
//        batch.begin();
//        sprBrick.draw(batch);
//        batch.end();

        //borders
        float borderY = 20;
        float step = 20;
        while (borderY < Gdx.graphics.getHeight() - 45) {
            sprLeftBorder.setSize(5, step);
            sprLeftBorder.setPosition(0, borderY);
            borderY += step;

            batch.begin();
            sprLeftBorder.draw(batch);
            batch.end();
        }

        borderY = 20;
        while (borderY < Gdx.graphics.getHeight() - 45) {
            sprLeftBorder.setSize(5, step);
            sprLeftBorder.setPosition(206, borderY);
            borderY += step;

            batch.begin();
            sprLeftBorder.draw(batch);
            batch.end();
        }

        float borderX = 5;
        step = 20;
        while (borderX < Gdx.graphics.getWidth() - 65) {
            sprTopBorder.setSize(step, 5);
            sprTopBorder.setPosition(borderX, Gdx.graphics.getHeight() - 38);
            borderX += step;

            batch.begin();
            sprTopBorder.draw(batch);
            batch.end();
        }

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

    public void resize(int width, int height) {
        //cam.viewportWidth = (Consts.VIEWPORT_HEIGHT / height) * width;
        //cam.update();
    }

    @Override
    public void dispose() {

    }
}
