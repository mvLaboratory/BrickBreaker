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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by MV on 21.03.2015.
 */
public class Border {
    private float x, y, width, height;
    private World physicWorld;
    private Body physicBody;
    private BodyDef bodyDef;

    public Border(float x, float y, float width, float height, World physicWorld) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.physicWorld = physicWorld;

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
        fixtureDef.density = 100f;
        fixtureDef.restitution = 0;

        Fixture fixture = physicBody.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        bodyShape.dispose();
    }

    public float getX() {
        return GameHelpers.meterToCoordX(x) - (width / GameHelpers.screenDensity());
    }

    public float getY() {
        return GameHelpers.meterToCoordY(y) - (height / GameHelpers.screenDensity());
    }

    public float getWidth() {
        return GameHelpers.meterToPixelsX(width * 2);
    }

    public float getHeight() {
        return GameHelpers.meterToPixelsY(height * 2);
    }
}
