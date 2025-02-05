

package com.MVlab.BrickBreaker.gameObjects;

import com.MVlab.BrickBreaker.gameWorld.GameWorld;
import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GameHelpers;
import com.MVlab.BrickBreaker.utils.LevelLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.MVlab.BrickBreaker.utils.MV_Math;

public class Racket {
    Vector2 position;

    private float width;
    private float height;
    private float fullHeight;
    private boolean deleted;

    private Body physicBody;
    BodyDef bodyDef;
    World physicWorld;
    GameWorld gameWorld;

    float startPositionX;
    float startPositionY;
    float targetPosition = 0;

    public Racket(float x, float y, float width, float height, float fullHeight, World physicWorld, GameWorld gameWorld) {
        this.startPositionX = x;
        this.startPositionY = y;
        this.physicWorld = physicWorld;
        this.fullHeight = fullHeight;
        this.height = height;
        this.width = width;
        this.gameWorld = gameWorld;

        position = new Vector2(x, y);
        targetPosition = x * 2;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position);

        physicBody = physicWorld.createBody(bodyDef);
        physicBody.setType(BodyDef.BodyType.KinematicBody);
        physicBody.setUserData(this);

        PolygonShape bodyShape = new PolygonShape();

//        bodyShape.setAsBox(width, height);
        Vector2[] vertices = new Vector2[8];
        vertices[0] = new Vector2(x - width, y - height * 2);
        vertices[1] = new Vector2(x + width, y - height * 2);
        vertices[2] = new Vector2(x - (width / 1.2f), y - height);
        vertices[3] = new Vector2(x + (width / 1.2f), y - height);
        vertices[4] = new Vector2(x - width / 3, y);
        vertices[5] = new Vector2(x + width / 3, y);
        //bottom
        vertices[6] = new Vector2(x - width, y - fullHeight);
        vertices[7] = new Vector2(x + width, y - fullHeight);
        bodyShape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodyShape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 1.7f;

        physicBody.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        bodyShape.dispose();
    }

    public void update() {
        setRocketSpeed();
        if (LevelLoader.survival&& gameWorld.getExtraLivesCount() > 0)
            checkBrickCollision();
    }

    public void onClick(float x) {
        x = MathUtils.clamp(GameHelpers.coordToMeterX(x), Consts.GAME_LEFT_BORDER + width + 0.4f, Consts.GAME_RIGHT_BORDER - width - 0.2f);
        targetPosition = x;
        setRocketSpeed();
    }

    public void onDrag(float x) {
        x = MathUtils.clamp(GameHelpers.coordToMeterX(x), Consts.GAME_LEFT_BORDER + width + 0.4f, Consts.GAME_RIGHT_BORDER - width - 0.2f);
        targetPosition = x;
        setRocketSpeed();
    }

    public void delete () {
        this.deleted = true;
    }

    public boolean existing() {
        return !deleted;
    }

    public void checkBrickCollision() {
        float posX = physicBody.getPosition().x + startPositionX;
        for (Brick brick : gameWorld.bricks) {
            if (!brick.existing()) continue;
            if (brick.getBrickY() > startPositionY * 1.95f) continue;
            if (brick.getBrickX() + brick.getBrickWidth() < posX - width * 0.9f) continue;
            if (brick.getBrickX() > posX + width * 1.2f) continue;
            gameWorld.destroyBrick(brick);
            gameWorld.racketDestroy();
        }
    }

    private void setRocketSpeed() {
        float bodyCenterX = physicBody.getPosition().x + startPositionX;
        float positionDelta = (targetPosition - bodyCenterX);
        float absPositionDelta = MV_Math.abs(positionDelta);

        Vector2 presentVelocity = physicBody.getLinearVelocity();

        if (absPositionDelta < 0.1)
            presentVelocity.x = 0;
        else presentVelocity.x = positionDelta * 50 / (Gdx.graphics.getDeltaTime() * 50);


        physicBody.setLinearVelocity(presentVelocity);
    }

    public void setRocketSpeed(float speed) {
        targetPosition = 0;
        float x = physicBody.getPosition().x + startPositionX + speed;
        x = MathUtils.clamp(x, Consts.GAME_LEFT_BORDER + width + 0.3f, Consts.GAME_RIGHT_BORDER - width - 0.3f);
        targetPosition = x;
        setRocketSpeed();
    }

    public float getX() {
        return GameHelpers.meterToCoordX(physicBody.getPosition().x) + GameHelpers.meterToPixelsX(startPositionX) - (width / GameHelpers.screenDensity());
    }

    public float getY() {
       return GameHelpers.meterToCoordY(physicBody.getPosition().y) + GameHelpers.meterToPixelsY(startPositionY) - (fullHeight / GameHelpers.screenDensityY());
    }

    public float getWidth() {
        return GameHelpers.meterToPixelsX(width) * 2;
    }

    public float getHeight() {
        return GameHelpers.meterToPixelsY(height);
    }

    public float getFullHeight() {
        return GameHelpers.meterToPixelsY(fullHeight);
    }
}
