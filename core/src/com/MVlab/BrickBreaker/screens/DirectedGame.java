package com.MVlab.BrickBreaker.screens;

import com.MVlab.BrickBreaker.Assets;
import com.MVlab.BrickBreaker.utils.AudioManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.MVlab.BrickBreaker.screens.transitions.Transitions.*;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by MV on 12.06.2015.
 */
public abstract class DirectedGame implements ApplicationListener {
    private boolean init;
    private AbstractGameScreen currScreen;
    private AbstractGameScreen nextScreen;
    private FrameBuffer currFbo;
    private FrameBuffer nextFbo;
    private SpriteBatch batch;
    private float t;
    private ScreenTransition screenTransition;

    public void setScreen(AbstractGameScreen screen) {
        setScreen(screen, null);
    }

    public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition) {
        AudioManager.instance.play(Assets.instance.sounds.doors, 3);

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        if(!init) {
            currFbo = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);
            nextFbo = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);
            batch = new SpriteBatch();
            init = true;
        }

        nextScreen = screen;
        nextScreen.show();
        nextScreen.resize(w, h);
        nextScreen.render(0);
        if (currScreen != null) currScreen.pause();
        nextScreen.pause();
        Gdx.input.setInputProcessor(null);
        this.screenTransition = screenTransition;
        t = 0;
    }

    @Override
    public void render () {
        // get delta time and ensure an upper limit of one 60th second
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(),
                1.0f / 60.0f);
        if (nextScreen == null) {
            // no ongoing transition
            if (currScreen != null) currScreen.render(deltaTime);
        } else {
            // ongoing transition
            float duration = 0;
            if (screenTransition != null)
                duration = screenTransition.getDuration();
            // update progress of ongoing transition
            t = Math.min(t + deltaTime, duration);
            if (screenTransition == null || t >= duration) {
                //no transition effect set or transition has just finished
                if (currScreen != null) currScreen.hide();
                nextScreen.resume();
                // enable input for next screen
               Gdx.input.setInputProcessor(
                        nextScreen.getInputProcessor());
                // switch screens
                currScreen = nextScreen;
                nextScreen = null;
                screenTransition = null;
            } else {
                // render screens to FBOs
                currFbo.begin();
                if (currScreen != null) currScreen.render(deltaTime);
                currFbo.end();
                nextFbo.begin();
                nextScreen.render(deltaTime);
                nextFbo.end();
                // render transition effect to screen
                float alpha = t / duration;

                screenTransition.render(batch,
                        currFbo.getColorBufferTexture(),
                        nextFbo.getColorBufferTexture(),
                        alpha);
            }
        }
    }

    @Override
    public void resize (int width, int height) {
        if (currScreen != null) currScreen.resize(width, height);
        if (nextScreen != null) nextScreen.resize(width, height);
    }
    @Override
    public void pause () {
        if (currScreen != null) currScreen.pause();
    }
    @Override
    public void resume () {
        if (currScreen != null) currScreen.resume();
    }
    @Override
    public void dispose () {
        if (currScreen != null) currScreen.hide();
        if (nextScreen != null) nextScreen.hide();
        if (init) {
            currFbo.dispose();
            currScreen = null;
            nextFbo.dispose();
            nextScreen = null;
            batch.dispose();
            init = false;
        }
    }
}
