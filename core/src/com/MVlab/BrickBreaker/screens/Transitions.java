package com.MVlab.BrickBreaker.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by MV on 11.06.2015.
 */
public class Transitions {
    public interface ScreenTransition {
        public float getDuration();

        public void render(SpriteBatch batch, Texture currScene, Texture nextScene, float alpha);
    }
}
