package com.MVlab.BrickBreaker.gameObjects;

import com.MVlab.BrickBreaker.utils.Consts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Created by MV on 04.04.2015.
 */
public class Brick {
    private float x, y, width, height;
    private World physicWorld;
    private Body physicBody;
    private BodyDef bodyDef;
    private int health;
    private boolean deleted;

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
        fixtureDef.restitution = 0f;

        Fixture fixture = physicBody.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        bodyShape.dispose();
    }

    public void update() {
        if (health <= 0 && existing()) {
            physicBody.setActive(false);
            physicWorld.destroyBody(physicBody);
            delete();
        }
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

    public void delete () {
        this.deleted = true;
    }

    public boolean existing() {
        return !deleted;
    }

    public float getWidth() {
        float density = (Consts.VIEWPORT_WIDTH / 2) / (Gdx.graphics.getWidth() / 2);
        float x = width;
        return (x / density) * 2;
    }

    public float getHeight() {
        float density = (Consts.VIEWPORT_HEIGHT / 2) / (Gdx.graphics.getHeight() / 2);
        float y = height;
        return (y / density) * 2;
    }

    public float getX() {
        float density = (Consts.VIEWPORT_WIDTH / 2) / (Gdx.graphics.getWidth() / 2);
        float x = physicBody.getPosition().x;
        return (x / density) + (Gdx.graphics.getWidth() / 2) - (width / density);
    }

    public float getY() {
        float density = (Consts.VIEWPORT_HEIGHT / 2) / (Gdx.graphics.getHeight() / 2);
        float y = physicBody.getPosition().y;
        return (y / density) + (Gdx.graphics.getHeight() / 2) - (height / density);
    }


//returns brick parameters in pixels
    public float getBrickWidth() {
        return width ;
    }

    public float getBrickHeight() {
        return height;
    }

    public float getBrickX() {
        return x;
    }

    public float getBrickY() {
        return y;
    }
}
