/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.gameObjects;

import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GameHelpers;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.MVlab.BrickBreaker.utils.MV_Math;

public class Racket {
    private Vector2 position;

    private float width;
    private float height;

    private Body physicBody;
    private BodyDef bodyDef;
    private World physicWorld;

    float startPosition;
    float targetPosition = 0;

    public Racket(float x, float y, float width, float height, World physicWorld) {
        this.startPosition = x;
        this.physicWorld = physicWorld;

        position = new Vector2(x, y);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
//        bodyDef.position.set(position);

        physicBody = physicWorld.createBody(bodyDef);
        physicBody.setType(BodyDef.BodyType.KinematicBody);

        PolygonShape bodyShape = new PolygonShape();

//        bodyShape.setAsBox(width, height);
        Vector2[] vertices = new Vector2[8];
        vertices[0] = new Vector2(x - width, y);
        vertices[1] = new Vector2(x + width, y);
        vertices[2] = new Vector2(x - (width / 1.2f), y + height);
        vertices[3] = new Vector2(x + (width / 1.2f), y + height);
        vertices[4] = new Vector2(x - width / 3, y + height * 2);
        vertices[5] = new Vector2(x + width / 3, y + height * 2);
        //bottom
        vertices[6] = new Vector2(x - width / 3, Consts.GAME_BOTTOM_BORDER);
        vertices[7] = new Vector2(x + width / 3, Consts.GAME_BOTTOM_BORDER);
        bodyShape.set(vertices);

        this.height = (y + height * 2) - Consts.GAME_BOTTOM_BORDER;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodyShape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 2.5f;

        Fixture fixture = physicBody.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        bodyShape.dispose();
    }

    public void update(float delta) {
        float bodyCenterX = physicBody.getPosition().x + startPosition;
        float positionDelta = (targetPosition - bodyCenterX);
        float absPositionDelta = MV_Math.abs(positionDelta);

        Vector2 presentVelocity = physicBody.getLinearVelocity();

        if (absPositionDelta < 1) {
           if (absPositionDelta < 0.01)
            presentVelocity.x = 0;
           else presentVelocity.x = positionDelta * 50;
       }
        physicBody.setLinearVelocity(presentVelocity);
    }

    public void onClick(float x) {
        x = MathUtils.clamp(GameHelpers.coordToMeterX(x), Consts.GAME_LEFT_BORDER + width, Consts.GAME_RIGHT_BORDER - width);
        targetPosition = x;
        float bodyCenterX = physicBody.getPosition().x + startPosition;
        float positionDelta = (targetPosition - bodyCenterX);
        float absPositionDelta = MV_Math.abs(positionDelta);

        Vector2 presentVelocity = physicBody.getLinearVelocity();
        presentVelocity.x = positionDelta > 0 ? 100 : -100;
        if (absPositionDelta < 1) {
            if (absPositionDelta < 0.01)
                presentVelocity.x = 0;
            else presentVelocity.x = positionDelta * 50;
        }
        physicBody.setLinearVelocity(presentVelocity);
    }

    public void onDrag(float x) {
        x = MathUtils.clamp(GameHelpers.coordToMeterX(x), Consts.GAME_LEFT_BORDER + width, Consts.GAME_RIGHT_BORDER - width);
        targetPosition = x;
        float bodyCenterX = physicBody.getPosition().x + startPosition;
        float positionDelta = (targetPosition - bodyCenterX);
        float absPositionDelta = MV_Math.abs(positionDelta);

        Vector2 presentVelocity = physicBody.getLinearVelocity();
        presentVelocity.x = positionDelta > 0 ? 100 : -100;
        if (absPositionDelta < 1) {
            if (absPositionDelta < 0.01)
                presentVelocity.x = 0;
            else presentVelocity.x = positionDelta * 50;
        }
        physicBody.setLinearVelocity(presentVelocity);
    }

    public float getX() {
        return GameHelpers.meterToCoordX(physicBody.getPosition().x) - (width / GameHelpers.screenDensity() * 1.5f);
    }

    public float getY() {
        return GameHelpers.meterToCoordY(physicBody.getPosition().y) - (height / GameHelpers.screenDensity() * 2f);
    }

    public float getWidth() {
        return GameHelpers.meterToPixelsX(width);
    }

    public float getHeight() {
        return GameHelpers.meterToPixelsY(height);
    }
}
