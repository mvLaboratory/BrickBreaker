package com.MVlab.BrickBreaker.gameObjects;

import com.MVlab.BrickBreaker.utils.GameHelpers;

public class GameButton {
    private float x, y, width, height;

    public GameButton(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isButtonPressed(float x, float y) {
        float posX = GameHelpers.pixelsToMeterX_GUI(x);
        float posY = GameHelpers.coordsToMeterY_GUI(y);
        if ((posX > getX() && posX < getX() + getWidth()) && (posY > getY() && posY < getY() + getHeight())) return true;

        return false;
    }

    public float getX() {
        return GameHelpers.GUI_camWidth - this.x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
