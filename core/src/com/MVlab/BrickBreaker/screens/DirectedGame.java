package com.MVlab.BrickBreaker.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.MVlab.BrickBreaker.screens.Transitions.*;

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
}
