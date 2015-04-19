/**
 * Created by MV on 18.03.2015.
 */
package com.MVlab.BrickBreaker.gameObjects;

import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GameHelpers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ball {
    private World physicWorld;
    private float radius;
    private Vector2 physicPosition;
    private Body physicBody;
    private BodyDef bodyDef;
    private Fixture fixture;
    private CircleShape ballShape;


    public Ball(float x, float y, float radius, World physicWorld) {
        physicPosition = new Vector2(x, y);

        this.radius = radius;
        this.physicWorld = physicWorld;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(physicPosition);

        physicBody = physicWorld.createBody(bodyDef);
        physicBody.setType(BodyDef.BodyType.DynamicBody);

        ballShape = new CircleShape();
        ballShape.setRadius(radius);
        ballShape.setPosition(physicPosition);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ballShape;
        fixtureDef.density = 50f;
        fixtureDef.restitution = 0.5f;

        fixture = physicBody.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        ballShape.dispose();
    }

    public void onClick(float x) {

    }

    public void update() {
        Vector2 presentVelocity = physicBody.getLinearVelocity();
        if (presentVelocity.y > 25) presentVelocity.y = 25;
        if (presentVelocity.x > 25) presentVelocity.x = 25;
        physicBody.setLinearVelocity(presentVelocity);
    }

    public float getX() {
        float density = (Consts.VIEWPORT_WIDTH / 2) / (Gdx.graphics.getWidth() / 2);
        float x = physicBody.getPosition().x;
        return (x / density) + (Gdx.graphics.getWidth() / 2) - (radius / density * 4f);
    }

    public float getY() {
        float density = (Consts.VIEWPORT_HEIGHT / 2) / (Gdx.graphics.getHeight() / 2);
        float y = physicBody.getPosition().y;
        return (y / density) + (Gdx.graphics.getHeight() / 2) - (radius / density);
    }

    public float getRadius() {
        return GameHelpers.meterToPixelsY(radius * 2);
    }

    public Vector2 getPosition() {
        return physicBody.getPosition();
    }
}
