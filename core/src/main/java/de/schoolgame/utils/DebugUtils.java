package de.schoolgame.utils;

import com.badlogic.gdx.Gdx;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.Camera;
import de.schoolgame.state.GameState;

public class DebugUtils {
    public static Vec2i getTilePosFromScreenPos(Vec2i screen) {
        screen.y = (-screen.y) + Gdx.graphics.getHeight();

        Camera camera = GameState.INSTANCE.camera;
        final int tileSize = GameState.INSTANCE.world.getTileSize();

        Vec2i div = new Vec2i(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
            .div(camera.viewSize);

        Vec2f origin = camera.position.toVec2f().sub(camera.viewSize.div(2).toVec2f());

        Vec2f world = origin.add(screen.div(div).toVec2f());

        return world.div(tileSize).toVec2i();
    }
}
