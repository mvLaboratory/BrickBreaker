package com.MVlab.BrickBreaker.utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by MV on 13.04.2015.
 */
public class GameHelpers {
    public static float screenDensity() {
        return (Consts.VIEWPORT_WIDTH / 2) / (Gdx.graphics.getWidth() / 2);
    }

    public static float pixelToMeterX(float x) {
        return x * screenDensity();
    }

    public static float meterToPixelsX(float x) {
        return x / screenDensity();
    }

    public static float coordToMeterX(float x) {
        return pixelToMeterX(x) - (Consts.VIEWPORT_WIDTH / 2);
    }

    public static float meterToCoordX(float x) {
        return meterToPixelsX(x) + (Gdx.graphics.getWidth() / 2);
    }

    public static float pixelToMeterY(float y) {
        return y * screenDensity();
    }

    public static float meterToPixelsY(float y) {
        return y / screenDensity();
    }

    public static float coordToMeterY(float y) {
        return pixelToMeterY(y) - (Consts.VIEWPORT_HEIGHT / 2);
    }

    public static float meterToCoordY(float y) {
        return meterToPixelsY(y) + (Gdx.graphics.getHeight() / 2);
    }
}
