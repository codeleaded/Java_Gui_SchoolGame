package de.schoolgame.utils;

import com.badlogic.gdx.Gdx;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.Camera;
import de.schoolgame.state.GameState;

public class CoordinateUtils {
    public static Vec2i getCameraPosFromScreenPos(Vec2i screen) {
        screen.y = (-screen.y) + Gdx.graphics.getHeight();

        Camera camera = GameState.INSTANCE.camera;
        Vec2f viewSize = camera.viewSize.toVec2f().mul(camera.zoom);

        Vec2f div = new Vec2i(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).toVec2f().div(viewSize);

        Vec2f cam = screen.toVec2f().div(div);

        return cam.toVec2i();
    }

    public static Vec2i getTilePosFromScreenPos(Vec2i screen) {
        screen.y = (-screen.y) + Gdx.graphics.getHeight();

        Camera camera = GameState.INSTANCE.camera;
        Vec2f viewSize = camera.viewSize.toVec2f().mul(camera.zoom);

        Vec2f div = new Vec2i(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).toVec2f().div(viewSize);

        Vec2f origin = camera.position.toVec2f().sub(viewSize.div(2));

        Vec2f world = origin.add(screen.toVec2f().div(div));

        final int tileSize = GameState.INSTANCE.world.getTileSize();

        return world.div(tileSize).toVec2i();
    }
}
