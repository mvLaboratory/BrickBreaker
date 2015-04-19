package com.MVlab.BrickBreaker.gameObjects;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by MV on 19.04.2015.
 */
public class RightBorder  extends Border {
    public RightBorder(float x, float y, float width, float height, World physicWorld) {
        super(x, y, width, height, physicWorld);
    }
}
