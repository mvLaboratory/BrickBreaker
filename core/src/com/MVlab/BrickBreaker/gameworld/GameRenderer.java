package com.MVlab.BrickBreaker.gameWorld;

import com.MVlab.BrickBreaker.Assets;
import com.MVlab.BrickBreaker.gameObjects.Border;
import com.MVlab.BrickBreaker.gameObjects.Brick;
import com.MVlab.BrickBreaker.gameObjects.GameButton;
import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GameHelpers;
import com.MVlab.BrickBreaker.utils.GamePreferences;
import com.MVlab.BrickBreaker.utils.LevelLoader;
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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    Sprite background, background2, background3, pauseButton, sprRacket, sprBall, sprExtraLive, sprSideBorder, sprTopBorder, sprBrick;
    private OrthographicCamera cam, guiCam;
    private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
    Box2DDebugRenderer b2debugRenderer;

    public GameRenderer(GameWorld world) {
        this.world = world;
        cam = new OrthographicCamera(Consts.VIEWPORT_WIDTH, Consts.VIEWPORT_HEIGHT);
        guiCam = new OrthographicCamera(Consts.VIEWPORT_GUI_WIDTH, Consts.VIEWPORT_GUI_HEIGHT);
        init();
    }

    public void init() {

        b2debugRenderer = new Box2DDebugRenderer(true, false, false, false, false, false);
        this.physicWorld = world.getPhysicWorld();

        cam.position.set(0, 0, 0);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
        sprSideBorder.flip(false, true);

        TextureRegion topBorderTexture = Assets.instance.topBorder.topBorder;
        sprTopBorder = new Sprite(topBorderTexture);

        TextureRegion ballTexture = Assets.instance.ball.ball;
        sprBall = new Sprite(ballTexture);
        sprBall.setSize(ball.getRadius(), ball.getRadius());

        sprExtraLive = new Sprite(ballTexture);

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

        TextureRegion sprBackground2 = Assets.instance.levelDecoration.background2;
        background2 = new Sprite(sprBackground2);

        TextureRegion sprBackground3 = Assets.instance.levelDecoration.background3;
        background3 = new Sprite(sprBackground3);

        TextureRegion sprPauseButton  = Assets.instance.levelDecoration.pauseButton;
        pauseButton = new Sprite(sprPauseButton);
    }

    public void render() {
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderObjects();
        renderGUI();

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

    private void renderObjects() {
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

        //Ball+++
        if (!(world.getPresentGameState() == GameWorld.gameState.dropped || world.getPresentGameState() == GameWorld.gameState.gameOver)) {
            sprBall.setPosition(ball.getX(), ball.getY());
            batch.begin();
            sprBall.draw(batch);
            batch.end();
        }
        //Ball---

        //racket+++
        if (racket.existing()) {
            sprRacket.setPosition(racket.getX(), racket.getY());
            batch.begin();
            sprRacket.draw(batch);
            batch.end();
        }
        //racket---

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
        float step = 100;

        float borderPosition = rightBorder.getY() - 100;
        float borderFinish = rightBorder.getY() + rightBorder.getHeight();
        while (borderPosition < borderFinish) {
            sprSideBorder.setSize(rightBorder.getWidth(), step);
            sprSideBorder.setPosition(rightBorder.getX(), borderPosition);
            borderPosition += step - 17;

            batch.begin();
            sprSideBorder.draw(batch);
            batch.end();
        }

        borderPosition = leftBorder.getY() - 100;
        borderFinish = leftBorder.getY() + leftBorder.getHeight();

        while (borderPosition < borderFinish) {
            sprSideBorder.setSize(leftBorder.getWidth(), step);
            sprSideBorder.setPosition(leftBorder.getX(), borderPosition);
            borderPosition += step - 17;

            batch.begin();
            sprSideBorder.draw(batch);
            batch.end();
        }

        borderPosition = topBorder.getX();
        borderFinish = topBorder.getX() + topBorder.getWidth();
        while (borderPosition < borderFinish) {
            sprTopBorder.setSize(step, topBorder.getHeight());
            sprTopBorder.setPosition(borderPosition, topBorder.getY());
            borderPosition += (step - 17);

            batch.begin();
            sprTopBorder.draw(batch);
            batch.end();
        }
        //Borders---

        //Background3+++
        batch.begin();
        background3.setSize(Gdx.graphics.getWidth() - (rightBorder.getX() / 2), Gdx.graphics.getHeight());
        background3.setOrigin(Gdx.graphics.getWidth() - (rightBorder.getX() / 2), 0);
        background3.setPosition(rightBorder.getX() + rightBorder.getWidth(), 0);
        background3.draw(batch);
        batch.end();
        //Background---

        //Background2+++
        batch.begin();
        background2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - (topBorder.getY() / 2));
        background2.setOrigin(0, Gdx.graphics.getHeight() - (topBorder.getY() / 2));
        background2.setPosition(0, topBorder.getY() + topBorder.getHeight());
        background2.draw(batch);
        batch.end();
        //Background---


        batch.begin();
        world.splashParticles.draw(batch);
        world.racketExplosion.draw(batch);
        batch.end();
    }

    private void renderGUI() {
        SpriteBatch guiBatch = new SpriteBatch();
        guiBatch.setProjectionMatrix(guiCam.combined);
        //FPS
        if (GamePreferences.instance.showFpsCounter) {
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
            fpsFont.draw(guiBatch, "FPS: " + fps, guiCam.viewportWidth - 40, 10);
            guiBatch.end();
        }
        if (GamePreferences.instance.useAccelerometer) {
            //
            //Accelerometer debug
            guiBatch.begin();
            BitmapFont accDebugFont = Assets.instance.fonts.defaultSmall;
            accDebugFont.setColor(0, 1, 0, 1);
            accDebugFont.draw(guiBatch, world.debugAccelerometerMassage, guiCam.viewportWidth - 40, 20);
            guiBatch.end();
            //--
        }

        //score
        float scoreFntScale = (guiCam.viewportWidth / guiCam.viewportHeight * 0.6f) + 0.6f;
        guiBatch.begin();
        BitmapFont scoreFont =  Assets.instance.fonts.tableNormal;
        scoreFont.setScale(scoreFntScale);
        scoreFont.setColor(0, 1, 0, 1);
        float scoreWide = 17 * 5 * scoreFntScale;
        //scoreFont.draw(guiBatch, GameHelpers.getFormattedScore(world.getScore()), leftBorder.getX(), topBorder.getY() + (topBorder.getHeight() * 2) + 20);
        scoreFont.draw(guiBatch, GameHelpers.getFormattedScore(world.getScore()), 3, guiCam.viewportHeight - 5);
        guiBatch.end();
        //

        //Time
        float timeFntScale = (guiCam.viewportWidth / guiCam.viewportHeight * 0.6f) + 0.6f;
        guiBatch.begin();
        BitmapFont timeFont =  Assets.instance.fonts.tableNormal;
        timeFont.setScale(timeFntScale);
        timeFont.setColor(0, 1, 0, 1);
        // timeFont.draw(guiBatch, GameHelpers.getFormattedTime(world.getGameDuration()), leftBorder.getX() + 100, topBorder.getY() + (topBorder.getHeight() * 2) + 20);
        timeFont.draw(guiBatch, GameHelpers.getFormattedTime(world.getGameDuration()), scoreWide, guiCam.viewportHeight - 5);
        guiBatch.end();
        //

        //Extra lives+++
        guiBatch.begin();
        float sprRadius = (guiCam.viewportWidth / guiCam.viewportHeight * 11) + 10;
        sprExtraLive.setSize(sprRadius, sprRadius);
        float sprExtraLiveStartPosition = guiCam.viewportWidth - sprRadius * 1.1f;

        for (int i = 0; i < Consts.EXTRA_LIFE_CONT; i++) {
            if (i >= world.getExtraLivesCount())
                sprExtraLive.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            else
                sprExtraLive.setColor(1, 1, 1, 1);
            sprExtraLive.setPosition(sprExtraLiveStartPosition - (i * sprRadius) - (i), guiCam.viewportHeight - sprRadius * 1.6f);
            sprExtraLive.draw(guiBatch);
        }
        guiBatch.end();
        //--

        //start massage
        guiBatch.begin();
        if  (world.getPresentGameState() == GameWorld.gameState.restart || world.getPresentGameState() == GameWorld.gameState.start) {
            BitmapFont startMassageFont = Assets.instance.fonts.defaultBig;
            startMassageFont.setColor(1, 1, 1, 1);

            String startMassage = (Gdx.app.getType() == Application.ApplicationType.Desktop) ? "Click to start": "Touch to start";
            startMassageFont.drawMultiLine(guiBatch, startMassage, guiCam.viewportWidth / 2 - 10, guiCam.viewportHeight / 2 + 50, 0, BitmapFont.HAlignment.CENTER);
        }
        guiBatch.end();
        //

        //start massage
        guiBatch.begin();
        if  (world.levelStart()) {
            BitmapFont lvlStartMassageFont = Assets.instance.fonts.defaultBig;
            lvlStartMassageFont.setColor(1, 1, 1, 1);
            if (LevelLoader.survival) {
                lvlStartMassageFont.drawMultiLine(guiBatch, "TRY", guiCam.viewportWidth / 2 - 10, guiCam.viewportHeight / 2 + 17, 0, BitmapFont.HAlignment.CENTER);
                lvlStartMassageFont.drawMultiLine(guiBatch, "TO SURVIVE", guiCam.viewportWidth / 2 - 10, guiCam.viewportHeight / 2 - 17, 0, BitmapFont.HAlignment.CENTER);
            }
            else
                lvlStartMassageFont.drawMultiLine(guiBatch, "Level " + GameWorld.getLevelNumber(), guiCam.viewportWidth / 2 - 10, guiCam.viewportHeight / 2, 0, BitmapFont.HAlignment.CENTER);
        }
        guiBatch.end();
        //

        //start massage
        guiBatch.begin();
        if  (world.getPresentGameState() == GameWorld.gameState.gameOver) {
            BitmapFont gameOverMassageFont = Assets.instance.fonts.defaultBig;
            gameOverMassageFont.setColor(1, 1, 1, 1);
            gameOverMassageFont.drawMultiLine(guiBatch, "Game over", guiCam.viewportWidth / 2 - 10, guiCam.viewportHeight / 2 + 50, 0, BitmapFont.HAlignment.CENTER);
            String overMassage = (Gdx.app.getType() == Application.ApplicationType.Desktop) ? "Click to restart": "Touch to restart";
            gameOverMassageFont.drawMultiLine(guiBatch, overMassage, guiCam.viewportWidth / 2 - 10, guiCam.viewportHeight / 2, 0, BitmapFont.HAlignment.CENTER);
        }
        guiBatch.end();
        //

        guiBatch.begin();
        pauseButton.setPosition(world.pauseButton.getX(), world.pauseButton.getY());
        pauseButton.setSize( world.pauseButton.getWidth(), world.pauseButton.getHeight());
        pauseButton.draw(guiBatch);
        guiBatch.end();
    }

    public void resize(int width, int height) {
        //cam.viewportWidth = (Consts.VIEWPORT_HEIGHT / height) * width;
        //cam.update();
        guiCam.viewportWidth = Consts.VIEWPORT_GUI_HEIGHT / (float) height * (float) width;
        guiCam.viewportHeight = Consts.VIEWPORT_GUI_HEIGHT;
        guiCam.position.set(guiCam.viewportWidth / 2, guiCam.viewportHeight / 2, 0);
        GameHelpers.GUI_camHeight = guiCam.viewportHeight;
        GameHelpers.GUI_camWidth = guiCam.viewportWidth;
        guiCam.update();
    }

    @Override
    public void dispose() {

    }
}
