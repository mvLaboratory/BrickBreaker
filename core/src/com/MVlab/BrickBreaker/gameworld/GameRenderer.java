/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.gameWorld;

import com.MVlab.BrickBreaker.Assets;
import com.MVlab.BrickBreaker.gameObjects.Border;
import com.MVlab.BrickBreaker.gameObjects.Brick;
import com.MVlab.BrickBreaker.gameObjects.LeftBorder;
import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GameHelpers;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    Sprite background, background2, background3, sprPipe, sprRacket, sprBall, sprSideBorder, sprTopBorder, sprBrick;
    private OrthographicCamera cam, guiCam;
    private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
    private Box2DDebugRenderer b2debugRenderer;

    public GameRenderer(GameWorld world) {
        this.world = world;
        cam = new OrthographicCamera(Consts.VIEWPORT_WIDTH, Consts.VIEWPORT_HEIGHT);
        guiCam = new OrthographicCamera(Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT);
        init();
    }

    public void init() {
        this.b2debugRenderer = new Box2DDebugRenderer(true, false, false, false, false, false);
        this.physicWorld = world.getPhysicWorld();

        cam.position.set(0, 0, 0);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        guiCam.viewportWidth = Consts.VIEWPORT_GUI_WIDTH;
//        guiCam.viewportHeight = Consts.VIEWPORT_GUI_HEIGHT;
//        guiCam.position.set(guiCam.viewportWidth / 2, guiCam.viewportHeight / 2, 0);
//
//        guiCam.setToOrtho(false);
//        guiCam.update();
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

        background2 = new Sprite(regions);
        background3 = new Sprite(regions);
    }

    public void render() {
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch batch = new SpriteBatch();
        //batch.setProjectionMatrix(cam.combined);
        //Background+++
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

        borderPosition = rightBorder.getY() - 100;
        borderFinish = rightBorder.getY() + rightBorder.getHeight();
        while (borderPosition < borderFinish) {
            sprSideBorder.setSize(rightBorder.getWidth(), step);
            sprSideBorder.setPosition(rightBorder.getX(), borderPosition);
            borderPosition += step;

            batch.begin();
            sprSideBorder.draw(batch);
            batch.end();
        }

        borderPosition = leftBorder.getY() - 100;
        borderFinish = leftBorder.getY() + leftBorder.getHeight();

        while (borderPosition < borderFinish) {
            sprSideBorder.setSize(leftBorder.getWidth(), step);
            sprSideBorder.setPosition(leftBorder.getX(), borderPosition);
            borderPosition += step;

            batch.begin();
            sprSideBorder.draw(batch);
            batch.end();
        }

        //Background2+++
        batch.begin();
        background2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - (topBorder.getY() / 2));
        background2.setOrigin(0, Gdx.graphics.getHeight() - (topBorder.getY() / 2));
        background2.setPosition(0, topBorder.getY() + topBorder.getHeight());
        background2.draw(batch);
        batch.end();
        //Background---

        //Background2+++
        batch.begin();
        background3.setSize(Gdx.graphics.getWidth() - (rightBorder.getX() / 2), Gdx.graphics.getHeight());
        background3.setOrigin(Gdx.graphics.getWidth() - (rightBorder.getX() / 2), 0);
        background3.setPosition(rightBorder.getX() + rightBorder.getWidth(), 0);
        background3.draw(batch);
        batch.end();
        //Background---

        SpriteBatch guiBatch = new SpriteBatch();
        guiBatch.setProjectionMatrix(guiCam.combined);
        //FPS
        guiBatch.begin();
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultSmall;
        if (fps >= 45)
            fpsFont.setColor(0, 1, 0, 1);
        else if (fps >= 30)
            fpsFont.setColor(1, 1, 0, 1);
        else
            fpsFont.setColor(1, 0, 0, 1);

       // fpsFont.draw(guiBatch, "FPS: " + fps, rightBorder.getX() + leftBorder.getWidth() + 10, 10);
        fpsFont.draw(guiBatch, "FPS: " + fps, Consts.VIEWPORT_GUI_WIDTH - 10, 10);
        guiBatch.end();
        //

        //score
        guiBatch.begin();
        BitmapFont scoreFont =  Assets.instance.fonts.tableNormal;
        scoreFont.setColor(0, 1, 0, 1);
        //scoreFont.draw(guiBatch, GameHelpers.getFormattedScore(world.getScore()), leftBorder.getX(), topBorder.getY() + (topBorder.getHeight() * 2) + 20);
        scoreFont.draw(guiBatch, GameHelpers.getFormattedScore(world.getScore()), 5, Consts.VIEWPORT_GUI_HEIGHT - 5);
        guiBatch.end();
        //

        //Time
        guiBatch.begin();
        BitmapFont timeFont =  Assets.instance.fonts.tableNormal;
        timeFont.setColor(0, 1, 0, 1);
       // timeFont.draw(guiBatch, GameHelpers.getFormattedTime(world.getGameDuration()), leftBorder.getX() + 100, topBorder.getY() + (topBorder.getHeight() * 2) + 20);
        timeFont.draw(guiBatch, GameHelpers.getFormattedTime(world.getGameDuration()), 110, Consts.VIEWPORT_GUI_HEIGHT - 5);
        guiBatch.end();
        //

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
       guiCam.viewportWidth = Consts.VIEWPORT_GUI_HEIGHT / (float) height * (float) width;
       guiCam.viewportHeight = Consts.VIEWPORT_GUI_HEIGHT;
       guiCam.position.set(guiCam.viewportWidth / 2, guiCam.viewportHeight / 2, 0);
       guiCam.update();
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
