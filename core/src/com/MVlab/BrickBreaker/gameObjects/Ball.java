/**
 * Created by MV on 18.03.2015.
 */
package com.MVlab.BrickBreaker.gameObjects;

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
        return physicBody.getPosition().x;
    }

    public float getY() {
        return physicBody.getPosition().y;
    }

    public float getRadius() {
        return radius;
    }

    public Vector2 getPosition() {
        return physicBody.getPosition();
    }
}
