package com.MVlab.BrickBreaker.utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by MV on 13.04.2015.
 */
public class GameHelpers {
    public static float screenDensity() {
        return (Consts.VIEWPORT_WIDTH / 2) / (Gdx.graphics.getWidth() / 2);
    }

    public static float screenDensityY() {
        return (Consts.VIEWPORT_HEIGHT / 2) / (Gdx.graphics.getHeight() / 2);
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
        return y * screenDensityY();
    }

    public static float meterToPixelsY(float y) {
        return y / screenDensityY();
    }

    public static float coordToMeterY(float y) {
        return pixelToMeterY(y) - (Consts.VIEWPORT_HEIGHT / 2);
    }

    public static float meterToCoordY(float y) {
        return meterToPixelsY(y) + (Gdx.graphics.getHeight() / 2);
    }

    public static String getFormattedScore(int score) {
        String returnScore = "00000";
        returnScore = returnScore + score;
        returnScore = returnScore.substring(returnScore.length() - 5);
        return returnScore;
    }

    public static String getFormattedTime(float secondsCount) {
        String strMinutes = "00";
        String strSeconds = "00";
        String strHours = "00";
        int minutes = 0;
        int hours = 0;
        int seconds = 0;

        minutes = (int)secondsCount / 60;
        seconds = (int)secondsCount - (minutes * 60);
        hours = (int) minutes / 60;
        minutes -= hours * 60;

        strHours += hours;
        strHours = strHours.substring(strHours.length() - 2);

        strMinutes += minutes;
        strMinutes = strMinutes.substring(strMinutes.length() - 2);

        strSeconds += seconds;
        strSeconds = strSeconds.substring(strSeconds.length() - 2);

        String returnString = strHours + ":" + strMinutes + ":" + strSeconds;
        return returnString;
    }
}
