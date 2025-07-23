package de.schoolgame.server;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

public class ServerLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static void createApplication() {
        new HeadlessApplication(new Server(), getDefaultConfiguration());
    }

    private static HeadlessApplicationConfiguration getDefaultConfiguration() {
        return new HeadlessApplicationConfiguration();
    }
}
