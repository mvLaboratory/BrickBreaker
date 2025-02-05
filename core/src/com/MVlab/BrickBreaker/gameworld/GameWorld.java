package com.MVlab.BrickBreaker.gameWorld;
import com.MVlab.BrickBreaker.Assets;
import com.MVlab.BrickBreaker.gameObjects.Border;
import com.MVlab.BrickBreaker.gameObjects.BottomBorder;
import com.MVlab.BrickBreaker.gameObjects.Brick;
import com.MVlab.BrickBreaker.gameObjects.GameButton;
import com.MVlab.BrickBreaker.gameObjects.LeftBorder;
import com.MVlab.BrickBreaker.gameObjects.RightBorder;
import com.MVlab.BrickBreaker.screens.DirectedGame;
import com.MVlab.BrickBreaker.screens.MenuScreen;
import com.MVlab.BrickBreaker.screens.transitions.ScreenTransitionSlide;
import com.MVlab.BrickBreaker.screens.transitions.Transitions;
import com.MVlab.BrickBreaker.utils.AudioManager;
import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.gameObjects.Ball;
import com.MVlab.BrickBreaker.gameObjects.Racket;
import com.MVlab.BrickBreaker.utils.LevelLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Interpolation;
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
    private Racket racket;
    private Ball ball;
    private LeftBorder leftBorder;
    private RightBorder rightBorder;
    private Border topBorder;
    BottomBorder bottomBorder;
    private World physicWorld;
    public ArrayList<Brick> bricks;
    private int score;
    private int extraLivesCount;
    private static int levelNumber;
    private float gameDuration, dropDuration, midLevelDuration, timeLeftTillReturnToMenu, midLinesDuration, timeToGameOver;
    private gameState presentGameState;
    private DirectedGame game;
    public GameButton pauseButton;
    public String debugAccelerometerMassage;
    public ParticleEffect splashParticles = new ParticleEffect();
    public ParticleEffect racketExplosion = new ParticleEffect();

    public enum gameState {start, restart, levelStart, levelRestart, active, paused, dropped, levelEnd, gameOver, gameRestart}

    public GameWorld(DirectedGame game) {
        this.game = game;
        extraLivesCount = Consts.EXTRA_LIFE_CONT;
        bricks = new ArrayList<Brick>();

        presentGameState = gameState.start;

        init();
    }

    public void init() {
        physicWorld = new World(new Vector2(0, -3f * getLevelMultiplier()), true);
        physicWorld.setContactListener(this);

        splashParticles.load(Gdx.files.internal("data/particles/splash.pfx"), Gdx.files.internal("data/particles"));
        splashParticles.scaleEffect(getSplashScale());

        racketExplosion.load(Gdx.files.internal("data/particles/splash.pfx"), Gdx.files.internal("data/particles"));
        racketExplosion.scaleEffect(getSplashScale() * 2.5f);

        racket = new Racket(Consts.GAME_CENTER, -2.3f, 1f, 0.2f, 3f, physicWorld, this);
        ball = new Ball(Consts.GAME_CENTER, -2.15f, 0.3f, physicWorld);

        leftBorder = new LeftBorder(Consts.GAME_LEFT_BORDER + 0.20f, -0.5f, 0.20f, Consts.GAME_TOP_BORDER, physicWorld);
        rightBorder = new RightBorder(Consts.GAME_RIGHT_BORDER, -0.5f, 0.20f, Consts.GAME_TOP_BORDER, physicWorld);
        topBorder = new Border(Consts.GAME_RIGHT_BORDER - ((Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2) - 0.01f, Consts.GAME_TOP_BORDER - 0.5f, (Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2, 0.20f, physicWorld);
        bottomBorder = new BottomBorder(Consts.GAME_RIGHT_BORDER - ((Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2) - 0.01f, Consts.GAME_BOTTOM_BORDER - 0.6f, (Consts.GAME_RIGHT_BORDER - Consts.GAME_LEFT_BORDER) / 2, 0.01f, physicWorld);

        pauseButton = new GameButton(55, 20, 55, 55);

        if (presentGameState == gameState.start || presentGameState == gameState.gameRestart){
            score = 0;
            gameDuration = 0;
            levelNumber = 0;
        }
        dropDuration = 0;
        midLevelDuration = 0;
        timeLeftTillReturnToMenu = 0;
        midLinesDuration = 0;
        timeToGameOver = 0;

        if (presentGameState == gameState.gameRestart) presentGameState = gameState.start;
        if (presentGameState == gameState.levelRestart) presentGameState = gameState.levelStart;

        initLevel();
    }

    private float getSplashScale() {
        return (Gdx.graphics.getHeight() + Gdx.graphics.getWidth()) / 680;
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
        levelNumber++;
        String[] lvlContent = LevelLoader.instance.loadLevel();

        bricks.clear();
        Vector2 brickPosition = new Vector2(-3.5f, 5.0f);

        float verticalShift = 0;
        float horizontalShift = 0;
        boolean isObjectSymbol;
        for (String brickSymbol : lvlContent) {
            isObjectSymbol = false;
            if (brickSymbol.equals("survival")) {
                midLinesDuration = Consts.TIME_BETWEEN_SURVIVAL_LINES;
                addBrickLines();
            } else if(LevelLoader.survival) LevelLoader.survival = false;

            if (brickSymbol.equals("\r")) continue;
            if (brickSymbol.equals("0")) isObjectSymbol = true;
            if (brickSymbol.equals("1")) {
                bricks.add(new Brick(brickPosition.x + horizontalShift, brickPosition.y + verticalShift, 0.4f, 0.25f, physicWorld));
                isObjectSymbol = true;
            }
            if (isObjectSymbol)
                horizontalShift += 1.3f;
            if (brickSymbol.equals("\n")) {horizontalShift = 0;  verticalShift -= 1;}
        }
    }

    public void addBrickLines() {
        Vector2 brickPosition = new Vector2(-3.5f, 5f);
        if (brickPosition.y < -3) {
            extraLivesCount = 0;
            presentGameState = gameState.gameOver;
        }

        for (int i = 0; i < Consts.MAX_SURVIVAL_LINES; i++) {
            for (int j = 0; j < Consts.BRICKS_PER_ROW; j++) {
                bricks.add(new Brick(brickPosition.x + j, brickPosition.y + i, 0.4f, 0.25f, physicWorld));
                brickPosition.x += 0.3;
            }
            brickPosition.x = -3.5f;
        }

        //for (Brick brick : bricks) brick.moveDown();

        //survivalLines++;
    }

    public boolean levelStart() {
        return presentGameState == gameState.levelStart || presentGameState == gameState.start;
    }
    //Levels---

    public void update(float delta) {
        if (active()) {
            physicWorld.step(delta, 1, 1);
            gameDuration += delta;

            splashParticles.update(delta);
            racketExplosion.update(delta);

            int activeBricksCount = 0;
            for (Brick brick : bricks) {
                if (brick != null) {
                    brick.update();
                    if (brick.existing()) activeBricksCount++;
                }
            }
            if (activeBricksCount == 0 && extraLivesCount > 0) {
                presentGameState = gameState.levelEnd;
                //initLevel();
            }

            racket.update();
            ball.update();
            if (ball.underBottomBorder()) {
                drop();
            }

            if (extraLivesCount <= 0) {
                timeToGameOver += delta;
                if (timeToGameOver >= Consts.TIME_TO_GAME_OVER) {
                    presentGameState = gameState.gameOver;
                    timeToGameOver = 0;
                }
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

        if (timeLeftTillReturnToMenu > 0) timeLeftTillReturnToMenu -= delta;
        if (timeLeftTillReturnToMenu < 0) {
            Transitions.ScreenTransition transition = ScreenTransitionSlide.init(0.50f,
                    ScreenTransitionSlide.RIGHT, false, Interpolation.pow5);
            game.setScreen(new MenuScreen(game), transition);
            timeLeftTillReturnToMenu = 0;
        }

        if (active() && LevelLoader.survival) {
            if (midLevelDuration > Consts.TIME_BETWEEN_SURVIVAL_LINES) {
                //addBrickLine();
                for (Brick brick : bricks) {
                    brick.moveDown();
                    if (brick.existing() && brick.getBrickY() < -6f) {
                        racketDestroy();
                    }
                }
                midLevelDuration = 0;
            }
            else midLevelDuration += delta;

            //if (survivalLines >= Consts.MAX_SURVIVAL_LINES) LevelLoader.survival = false;
        }
    }

    public void kickTheBall() {
        int ballDirectionX = MathUtils.random(-1, 2);
        ballDirectionX = ballDirectionX > 0 ? 1 : -1;
        ball.getPhysicBody().applyLinearImpulse(15 * ballDirectionX, 100 * getLevelMultiplier(), 0, 0, true);
        presentGameState = gameState.active;
        AudioManager.instance.play(Assets.instance.sounds.hit, 1 , MathUtils.random(1.0f, 1.1f));
    }

    public void destroyBrick(Brick brick) {
        splash(brick.getX(), brick.getY());

        brick.damage(100);

        int ballDirectionX = MathUtils.random(-1, 2);
        int ballDirectionY = ball.getY() > brick.getY() ? 2 : -1;
        ball.getPhysicBody().applyLinearImpulse(10 * ballDirectionX, 10 * ballDirectionY, 0, 0, true);

        scored();
    }

    public void splash(float x, float y) {
        splashParticles.setPosition(x, y);
        splashParticles.setFlip(false, true);
        splashParticles.start();

        AudioManager.instance.play(Assets.instance.sounds.explosion, 1 , MathUtils.random(1.0f, 1.1f));
    }

    public void splash(ParticleEffect effect, float x, float y) {
        effect.setPosition(x, y);
        effect.setFlip(false, true);
        effect.start();

        AudioManager.instance.play(Assets.instance.sounds.explosion, 1 , MathUtils.random(1.0f, 1.1f));
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

    public void gamePause() {
        if (active()) {
            setPresentGameState(gameState.paused);
            game.gamePause();
        }
        else if (presentGameState == gameState.paused) setPresentGameState(gameState.active);
    }

    public static int getLevelNumber() {
        return levelNumber;
    }

    public static float getLevelMultiplier() {
//        float levelMultiplier = MV_Math.max(1, levelNumber);
//        levelMultiplier = MV_Math.max(1f, levelMultiplier * 0.05f);
        return 1 + (levelNumber * 0.05f);
    }

    public void backToMenu() {
        timeLeftTillReturnToMenu = Consts.MID_SCREEN_DURATION;
//        game.setScreen(new MenuScreen(game));
    }

    public void racketDestroy() {
        splash(racketExplosion, racket.getX(), racket.getY() + racket.getFullHeight() / 2);
        racket.delete();
        extraLivesCount = 0;
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
            destroyBrick((Brick) bodyA);
        }

        if (bodyB instanceof Brick) {
            destroyBrick((Brick) bodyB);
        }

        //border collision
        if (bodyA instanceof Ball && bodyB instanceof LeftBorder) {
            ((Ball) bodyA).getPhysicBody().applyLinearImpulse(1, 0, 0, 0, true);
            AudioManager.instance.play(Assets.instance.sounds.hit, 1 , MathUtils.random(1.0f, 1.1f));
        }

        if (bodyA instanceof LeftBorder && bodyB instanceof Ball) {
            ((Ball) bodyB).getPhysicBody().applyLinearImpulse(1, 0, 0, 0, true);
            AudioManager.instance.play(Assets.instance.sounds.hit, 1 , MathUtils.random(1.0f, 1.1f));
        }

        if (bodyA instanceof Ball && bodyB instanceof RightBorder) {
            ((Ball) bodyA).getPhysicBody().applyLinearImpulse(-1, 0, 0, 0, true);
            AudioManager.instance.play(Assets.instance.sounds.hit, 1 , MathUtils.random(1.0f, 1.1f));
        }

        if (bodyA instanceof RightBorder && bodyB instanceof Ball) {
            ((Ball) bodyB).getPhysicBody().applyLinearImpulse(-1, 0, 0, 0, true);
            AudioManager.instance.play(Assets.instance.sounds.hit, 1 , MathUtils.random(1.0f, 1.1f));
        }

        if ((bodyA instanceof Ball && bodyB instanceof Border) || (bodyA instanceof Border && bodyB instanceof Ball)) {
            AudioManager.instance.play(Assets.instance.sounds.hit, 1 , MathUtils.random(1.0f, 1.1f));
        }

        //Bottom border
        if ((bodyA instanceof Ball && bodyB instanceof BottomBorder) || (bodyA instanceof BottomBorder && bodyB instanceof Ball)) {
            AudioManager.instance.play(Assets.instance.sounds.drop, 1 , MathUtils.random(1.0f, 1.1f));
            drop();
        }

        //Racket collision
        if ((bodyA instanceof Ball && bodyB instanceof Racket) || (bodyA instanceof Racket && bodyB instanceof Ball)) {
            AudioManager.instance.play(Assets.instance.sounds.hit, 1 , MathUtils.random(1.0f, 1.1f));
        }

    }

}
