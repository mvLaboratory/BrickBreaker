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
    }

    public void render() {
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        TextureRegion regions = Assets.instance.levelDecoration.background;
        Sprite spr = new Sprite(regions);
        spr.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        spr.draw(batch);
        batch.end();

        TextureRegion pipeTexture = Assets.instance.pipe.pipe;
        Sprite sprPipe = new Sprite(pipeTexture);
        sprPipe.setSize(300, 5);
        sprPipe.setPosition(0, 15);

        batch.begin();
        sprPipe.draw(batch);
        batch.end();

        TextureRegion racketTexture = Assets.instance.racket.racket;
        Sprite sprRacket = new Sprite(racketTexture);
        sprRacket.setSize(50, 100);
        sprRacket.setOrigin(25, 50);
        sprRacket.setPosition(racket.getX(), racket.getY());

        batch.begin();
        sprRacket.draw(batch);
        batch.end();

        TextureRegion ballTexture = Assets.instance.ball.ball;
        Sprite sprBall = new Sprite(ballTexture);
        sprBall.setSize(10, 10);
        sprBall.setPosition(ball.getX(), ball.getY());

        batch.begin();
        sprBall.draw(batch);
        batch.end();

        TextureRegion borderTexture = Assets.instance.border.border;
        Sprite sprLeftBorder = new Sprite(borderTexture);
        sprLeftBorder.setSize(25, 400);
        sprLeftBorder.setPosition(leftBorder.getX(), rightBorder.getY());

        batch.begin();
        sprLeftBorder.draw(batch);
        batch.end();

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
