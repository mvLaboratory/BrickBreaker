package com.MVlab.BrickBreaker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.MVlab.BrickBreaker.BrickBreakerCore;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Stuff breaker";
//        config.width = 272;
//        config.height = 408;
        config.width = 300;
        config.height = 450;
		new LwjglApplication(new BrickBreakerCore(), config);
	}
}
