package com.MVlab.BrickBreaker.gameObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashMap;

/**
 * Created by MV on 04.04.2015.
 */
public class Brick {
    private float x, y, width, height;
    private World physicWorld;
    private Body physicBody;
    private BodyDef bodyDef;
    private int health;

    public Brick(float x, float y, float width, float height, World physicWorld) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.physicWorld = physicWorld;
        this.health = 100;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        physicBody = physicWorld.createBody(bodyDef);
        physicBody.setType(BodyDef.BodyType.StaticBody);
        physicBody.setUserData(this);

        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodyShape;
        fixtureDef.density = 5f;
        fixtureDef.restitution = 0.5f;

        Fixture fixture = physicBody.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        bodyShape.dispose();
    }

    public void damage(int damage) {
        this.health -= damage;
    }

    public int getHealth() {
        return health;
    }

    public Body getBody() {
        return physicBody;
    }
}
