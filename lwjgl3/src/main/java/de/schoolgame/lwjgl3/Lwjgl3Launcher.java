package de.schoolgame.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        new Lwjgl3Application(new SplashScreen(), getSplashConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getSplashConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        configuration.setTitle("SchoolGame");
        configuration.setResizable(false);
        configuration.setWindowedMode(SplashScreen.WIDTH, SplashScreen.HEIGHT);
        configuration.setDecorated(false);
        configuration.setWindowPosition(-1, -1);

        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        configuration.setWindowIcon("logox128.png", "logox64.png", "logox32.png", "logox16.png");

        return configuration;
    }

    public static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        var displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();

        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        configuration.setTitle("SchoolGame");
        configuration.useVsync(true);

        configuration.setForegroundFPS(displayMode.refreshRate + 1);
        configuration.setWindowedMode(displayMode.width, displayMode.height);
        configuration.setResizable(false);
        configuration.setDecorated(false);

        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        configuration.setWindowIcon("logox128.png", "logox64.png", "logox32.png", "logox16.png");

        return configuration;
    }
}
