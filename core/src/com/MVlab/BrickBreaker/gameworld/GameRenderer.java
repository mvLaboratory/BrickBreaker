/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.gameWorld;

import com.MVlab.BrickBreaker.Assets;
import com.MVlab.BrickBreaker.gameObjects.Border;
import com.MVlab.BrickBreaker.gameObjects.Brick;
import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.MVlab.BrickBreaker.gameObjects.Ball;
import com.MVlab.BrickBreaker.gameObjects.Racket;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
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
    Sprite background, spr2, sprPipe, sprRacket, sprBall, sprSideBorder, sprTopBorder, sprBrick;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
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
        sprSideBorder = new Sprite(borderTexture);

        TextureRegion topBorderTexture = Assets.instance.topBorder.topBorder;
        sprTopBorder = new Sprite(topBorderTexture);

        TextureRegion ballTexture = Assets.instance.ball.ball;
        sprBall = new Sprite(ballTexture);
        sprBall.setSize(ball.getRadius(), ball.getRadius());

        TextureRegion racketTexture = Assets.instance.racket.racket;
        sprRacket = new Sprite(racketTexture);
        sprRacket.setSize(racket.getWidth(), racket.getFullHeight());
        sprRacket.setOrigin(racket.getWidth() / 2, 0);

//        TextureRegion pipeTexture = Assets.instance.pipe.pipe;
//        pipeTexture.flip(false, true);
//        sprPipe = new Sprite(pipeTexture);
//
        TextureRegion regions = Assets.instance.levelDecoration.background;
        background = new Sprite(regions);

        TextureRegion brickTexture = Assets.instance.brickTexture.brick;
        sprBrick = new Sprite(brickTexture);
    }

    public void render() {
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        SpriteBatch batch = new SpriteBatch();

        //Background+++
        batch.begin();
        background.draw(batch);
        batch.end();
        //Background---

//      sprPipe.setSize(300, 5);
//      sprPipe.setPosition(0, 15);
//      batch.begin();
//      sprPipe.draw(batch);
//      batch.end();

        //racket+++
        sprRacket.setPosition(racket.getX(), racket.getY());
        batch.begin();
        sprRacket.draw(batch);
        batch.end();
        //racket---

        //Ball+++
        sprBall.setPosition(ball.getX(), ball.getY());
        batch.begin();
        sprBall.draw(batch);
        batch.end();
        //Ball---

        //bricks+++
        for (Brick brick : bricks) {
            if (!brick.existing()) continue;
            sprBrick.setSize(brick.getWidth(), brick.getHeight());
            sprBrick.setPosition(brick.getX(), brick.getY());

            batch.begin();
            sprBrick.draw(batch);
            batch.end();
        }
        //bricks---

        //borders
        float borderPosition = topBorder.getX();
        float borderFinish = topBorder.getX() + topBorder.getWidth();
        float step = 20;

        while (borderPosition < borderFinish) {
            sprTopBorder.setSize(step, topBorder.getHeight());
            sprTopBorder.setPosition(borderPosition, topBorder.getY());
            borderPosition += step;

            batch.begin();
            sprTopBorder.draw(batch);
            batch.end();
        }

        borderPosition = rightBorder.getY();
        borderFinish = rightBorder.getY() + rightBorder.getHeight();
        while (borderPosition < borderFinish) {
            sprSideBorder.setSize(rightBorder.getWidth(), step);
            sprSideBorder.setPosition(rightBorder.getX(), borderPosition);
            borderPosition += step;

            batch.begin();
            sprSideBorder.draw(batch);
            batch.end();
        }

        borderPosition = leftBorder.getY();
        borderFinish = leftBorder.getY() + leftBorder.getHeight();

        while (borderPosition < borderFinish) {
            sprSideBorder.setSize(leftBorder.getWidth(), step);
            sprSideBorder.setPosition(leftBorder.getX(), borderPosition);
            borderPosition += step;

            batch.begin();
            sprSideBorder.draw(batch);
            batch.end();
        }

        if (DEBUG_DRAW_BOX2D_WORLD) {
            b2debugRenderer.render(physicWorld,
                    cam.combined);
        }

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

        //grid
//        for (int i = -10; i <= 0; i++) {
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
//            shapeRenderer.line(-10, i, 10, i);
//            shapeRenderer.end();
//        }
    }

    public void resize(int width, int height) {
        //cam.viewportWidth = (Consts.VIEWPORT_HEIGHT / height) * width;
        //cam.update();
    }

    @Override
    public void dispose() {

    }

    public void moveCamera(float x, float y) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Vector3 camPos = cam.position;
            cam.position.set(camPos.x + x, camPos.y + y, camPos.z);
        }
    }
}
