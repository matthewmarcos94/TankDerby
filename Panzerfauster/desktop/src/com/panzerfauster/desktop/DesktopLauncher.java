package com.panzerfauster.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.panzerfauster.GameState;
import com.panzerfauster.Panzerfauster;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Panzerfauster";
        config.width = 1000;
        config.height = 600;
        config.resizable = false;

        GameState.getState();
        new LwjglApplication(new Panzerfauster(), config);
    }
}
