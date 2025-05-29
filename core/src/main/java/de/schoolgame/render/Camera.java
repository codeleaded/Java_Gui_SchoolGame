package de.schoolgame.render;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
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
        zoom = 1;
    }

    public void update() {
        var state = GameState.INSTANCE;

        var halfView = viewSize.div(2).toVec2f();
        var tileSize = state.world.getTileSize();

        position = new Vec3f(state.player.getPosition().mul(tileSize), 0).clamp(new Vec3f(halfView, 0),
                new Vec3f(state.world.getSize().mul(tileSize).toVec2f().sub(halfView), 0));

        var h = viewSize.div(2);
        projectionMatrix.setToOrtho(zoom * -h.x, zoom * h.x, zoom * -h.y, zoom * h.y, 0, 100);
        viewMatrix.setToLookAt(new Vector3(0, 0, -1), new Vector3(0, 1, 0));
        viewMatrix.translate(-position.x, -position.y, -position.z);

        viewProjectionMatrix.set(projectionMatrix);
        viewProjectionMatrix.mul(viewMatrix);
    }
}
