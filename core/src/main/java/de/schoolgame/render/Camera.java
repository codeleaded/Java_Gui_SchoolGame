package de.schoolgame.render;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.primitives.Vec3f;
import de.schoolgame.state.GameState;

public class Camera {
    public Vec3f position;
    public float zoom;
    public Vec2i viewSize;

    public Matrix4 projectionMatrix = new Matrix4();
    public Matrix4 viewMatrix = new Matrix4();
    public Matrix4 viewProjectionMatrix = new Matrix4();

    public Camera() {
        viewSize = new Vec2i(640, 360);
        position = new Vec3f(viewSize.toVec2f().div(2), 0);
        zoom = 1f;
    }

    public void focusPlayer() {
        var state = GameState.INSTANCE;

        var tileSize = state.world.getTileSize();
        var worldSize = state.world.getSize().mul(tileSize).toVec2f();

        var halfView = this.viewSize.toVec2f().div(2).mul(zoom);

        position = new Vec3f(state.player.getRect().mid().mul(tileSize), 0)
            .clamp(
                new Vec3f(halfView, 0),
                new Vec3f(worldSize.sub(halfView), 0)
            );
    }

    public void setGui() {
        zoom = 1f;
        position = new Vec3f(viewSize.toVec2f().div(2), 0);
    }

    public void clampZoom() {
        var state = GameState.INSTANCE;

        int tileSize = state.world.getTileSize();
        Vec2f worldSize = state.world.getSize().mul(tileSize).toVec2f();

        Vec2f maxZoomVec = worldSize.div(viewSize.toVec2f());
        float maxZoom = Math.min(maxZoomVec.x, maxZoomVec.y);

        zoom = Math.clamp(zoom, 0f, Math.nextDown(maxZoom));
    }

    public void update() {
        var halfView = this.viewSize.toVec2f().div(2).mul(zoom);

        projectionMatrix.setToOrtho(-halfView.x, halfView.x, -halfView.y, halfView.y, 0, 100);
        viewMatrix.setToLookAt(new Vector3(0, 0, -1), new Vector3(0, 1, 0));
        viewMatrix.translate(-position.x, -position.y, -position.z);

        viewProjectionMatrix.set(projectionMatrix);
        viewProjectionMatrix.mul(viewMatrix);
    }
}
