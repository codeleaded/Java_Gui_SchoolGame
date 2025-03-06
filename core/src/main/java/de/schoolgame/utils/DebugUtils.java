package de.schoolgame.utils;

import com.badlogic.gdx.Gdx;
import de.schoolgame.render.Camera;
import de.schoolgame.state.GameState;

public class DebugUtils {
    public static int[] getTilePosFromScreenPos(int screenX, int screenY) {
        Camera camera = GameState.INSTANCE.camera;
        final int tileSize = GameState.INSTANCE.world.getTileSize();

        final int x_div = Gdx.graphics.getWidth() / camera.viewWidth;
        final int y_div = Gdx.graphics.getHeight() / camera.viewHeight;

        screenY = (-screenY) + Gdx.graphics.getHeight();

        float originX = camera.position.x - ((float) camera.viewWidth / 2);
        float originY = camera.position.y - ((float) camera.viewHeight / 2);

        float worldX = ((float) screenX / x_div) + originX;
        float worldY = ((float) screenY / y_div) + originY;

        int tileX = (int) worldX / tileSize;
        int tileY = (int) worldY / tileSize;

        return new int[]{tileX, tileY};
    }
}
