/**
 * Created by MV on 18.03.2015.
 */
package com.MVlab.BrickBreaker.gameObjects;

import com.MVlab.BrickBreaker.gameWorld.GameWorld;
import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GameHelpers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ball {
    private World physicWorld;
    float startPositionX;
    float startPositionY;
    private float radius;
    private Vector2 physicPosition;
    private Body physicBody;
    private BodyDef bodyDef;
    private Fixture fixture;
    private CircleShape ballShape;


    public Ball(float x, float y, float radius, World physicWorld) {
        physicPosition = new Vector2(x, y);
        this.startPositionX = x;
        this.startPositionY = y;

        this.radius = radius;
        this.physicWorld = physicWorld;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(physicPosition);

        physicBody = physicWorld.createBody(bodyDef);
        physicBody.setType(BodyDef.BodyType.DynamicBody);
        physicBody.setUserData(this);

        ballShape = new CircleShape();
        ballShape.setRadius(radius);
        ballShape.setPosition(physicPosition);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ballShape;
        fixtureDef.density = 40f;
        fixtureDef.restitution = 0.55f * GameWorld.getLevelMultiplier();

        fixture = physicBody.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        ballShape.dispose();
    }

    public void onClick(float x) {

    }

    public void update() {
        float ballMaxSpeed = 9 * GameWorld.getLevelMultiplier();
        Vector2 presentVelocity = physicBody.getLinearVelocity();
        presentVelocity.y = MathUtils.clamp(presentVelocity.y, -ballMaxSpeed, ballMaxSpeed);
        presentVelocity.x = MathUtils.clamp(presentVelocity.x, -ballMaxSpeed, ballMaxSpeed);

        physicBody.setLinearVelocity(presentVelocity);
    }

    public boolean underBottomBorder() {
        return getY() < Consts.GAME_BOTTOM_BORDER;
    }

    public float getX() {
        return  GameHelpers.meterToCoordX(physicBody.getPosition().x) + GameHelpers.meterToPixelsX(startPositionX) - (radius / GameHelpers.screenDensity());
    }

    public float getY() {
//        float density = (Consts.VIEWPORT_HEIGHT / 2) / (Gdx.graphics.getHeight() / 2);
//        float y = physicBody.getPosition().y;
//        return (y / density) + (Gdx.graphics.getHeight() / 2) - (radius / density);
        return GameHelpers.meterToCoordY(physicBody.getPosition().y) + GameHelpers.meterToPixelsY(startPositionY) - (radius / GameHelpers.screenDensityY());
    }

    public float getRadius() {
        return GameHelpers.meterToPixelsY(radius * 2);
    }

    public Vector2 getPosition() {
        return physicBody.getPosition();
    }

    public Body getPhysicBody() {
        return physicBody;
    }
}

