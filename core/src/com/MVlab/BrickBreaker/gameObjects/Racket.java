/**
 * Created by MV on 17.03.2015.
 */

package com.MVlab.BrickBreaker.gameObjects;

import com.MVlab.BrickBreaker.utils.Consts;
import com.MVlab.BrickBreaker.utils.GameHelpers;
import com.badlogic.gdx.Gdx;
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
    private float fullHeight;

    private Body physicBody;
    private BodyDef bodyDef;
    private World physicWorld;

    float startPositionX;
    float startPositionY;
    float targetPosition = 0;

    public Racket(float x, float y, float width, float height, float fullHeight, World physicWorld) {
        this.startPositionX = x;
        this.startPositionY = y;
        this.physicWorld = physicWorld;
        this.fullHeight = fullHeight;
        this.height = height;
        this.width = width;

        position = new Vector2(x, y);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position);

        physicBody = physicWorld.createBody(bodyDef);
        physicBody.setType(BodyDef.BodyType.KinematicBody);

        PolygonShape bodyShape = new PolygonShape();

//        bodyShape.setAsBox(width, height);
        Vector2[] vertices = new Vector2[8];
//        vertices[0] = new Vector2(x - width, y);
//        vertices[1] = new Vector2(x + width, y);
//        vertices[2] = new Vector2(x - (width / 1.2f), y + height);
//        vertices[3] = new Vector2(x + (width / 1.2f), y + height);
//        vertices[4] = new Vector2(x - width / 3, y + height * 2);
//        vertices[5] = new Vector2(x + width / 3, y + height * 2);

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
        fixtureDef.restitution = 1.5f;

        Fixture fixture = physicBody.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        bodyShape.dispose();
    }

    public void update(float delta) {
        setRocketSpeed();
//        float bodyCenterX = physicBody.getPosition().x + startPositionX;
//        float positionDelta = (targetPosition - bodyCenterX);
//        float absPositionDelta = MV_Math.abs(positionDelta);
//
//        Vector2 presentVelocity = physicBody.getLinearVelocity();
//
//        Gdx.app.debug("move", "11----");
//        Gdx.app.debug("move target", "" + targetPosition);
//        Gdx.app.debug("move center", "" + bodyCenterX);
//        Gdx.app.debug("move positionDelta", "" + positionDelta);
//        Gdx.app.debug("move velocity", "" + presentVelocity.x);
//        Gdx.app.debug("move", "11----");
//
//        if (absPositionDelta < 2) {
//           if (absPositionDelta < 0.01)
//            presentVelocity.x = 0;
//           else presentVelocity.x = positionDelta * 10;
//       }
//        Gdx.app.debug("move", "22----");
//        Gdx.app.debug("move target", "" + targetPosition);
//        Gdx.app.debug("move center", "" + bodyCenterX);
//        Gdx.app.debug("move positionDelta", "" + positionDelta);
//        Gdx.app.debug("move velocity", "" + presentVelocity.x);
//        Gdx.app.debug("move", "22----");
//
//        physicBody.setLinearVelocity(presentVelocity);
    }

    public void onClick(float x) {
        x = MathUtils.clamp(GameHelpers.coordToMeterX(x), Consts.GAME_LEFT_BORDER + width + 0.15f, Consts.GAME_RIGHT_BORDER - width - 0.15f);
        targetPosition = x;
        setRocketSpeed();
//        float bodyCenterX = physicBody.getPosition().x + startPositionX;
//        float positionDelta = (targetPosition - bodyCenterX);
//        float absPositionDelta = MV_Math.abs(positionDelta);
//
//        Vector2 presentVelocity = physicBody.getLinearVelocity();
//        presentVelocity.x = positionDelta > 0 ? 50 : -50;
//
//        Gdx.app.debug("click", "" + targetPosition);
//        Gdx.app.debug("click posdelta", "" + positionDelta);
//        Gdx.app.debug("click velocity", "" + presentVelocity.x);
//
//        if (absPositionDelta < 2) {
//            if (absPositionDelta < 0.01)
//                presentVelocity.x = 0;
//            else presentVelocity.x = positionDelta * 10;
//        }
//        physicBody.setLinearVelocity(presentVelocity);
    }

    public void onDrag(float x) {
        x = MathUtils.clamp(GameHelpers.coordToMeterX(x), Consts.GAME_LEFT_BORDER + width + 0.15f, Consts.GAME_RIGHT_BORDER - width - 0.15f);
        targetPosition = x;
        setRocketSpeed();
//        float bodyCenterX = physicBody.getPosition().x + startPositionX;
//        float positionDelta = (targetPosition - bodyCenterX);
//        float absPositionDelta = MV_Math.abs(positionDelta);
//
//        Vector2 presentVelocity = physicBody.getLinearVelocity();
//        presentVelocity.x = positionDelta > 0 ? 50 : -50;
//        if (absPositionDelta < 2) {
//            if (absPositionDelta < 0.01)
//                presentVelocity.x = 0;
//            else presentVelocity.x = positionDelta * 10;
//        }
//        physicBody.setLinearVelocity(presentVelocity);
    }

    private void setRocketSpeed() {
        float bodyCenterX = physicBody.getPosition().x + startPositionX;
        float positionDelta = (targetPosition - bodyCenterX);
        float absPositionDelta = MV_Math.abs(positionDelta);

        Vector2 presentVelocity = physicBody.getLinearVelocity();

//        if (absPositionDelta < 2) {
//            if (absPositionDelta < 0.01)
//                presentVelocity.x = 0;
//            else presentVelocity.x = positionDelta * 10;
//        }

            if (absPositionDelta < 0.1)
                presentVelocity.x = 0;
            else presentVelocity.x = positionDelta * 50 / (Gdx.graphics.getDeltaTime() * 50);


        physicBody.setLinearVelocity(presentVelocity);
    }

    public float getX() {
        return GameHelpers.meterToCoordX(physicBody.getPosition().x) - (width / GameHelpers.screenDensity());
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
