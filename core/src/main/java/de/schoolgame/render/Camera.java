package de.schoolgame.render;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.primitives.Vec3f;

public class Camera {
    public Vec3f position;
    public float zoom;
    public int viewWidth, viewHeight;

    public Matrix4 projectionMatrix = new Matrix4();
    public Matrix4 viewMatrix = new Matrix4();
    public Matrix4 viewProjectionMatrix = new Matrix4();

    public Camera() {
        viewWidth = 640;
        viewHeight = 360;
        position = new Vec3f((float) viewWidth / 2, (float) viewHeight / 2, 0);
        zoom = 1;
    }

    public void update() {
        var state = GameState.INSTANCE;
        position = new Vec3f(state.player.getPosition(), 0);
        position.x = Math.max(position.x, 320);
        position.x = Math.min(position.x, state.world.getWidth() * state.world.getTileSize() - 320);

        int hx = viewWidth / 2;
        int hy = viewHeight / 2;
        projectionMatrix.setToOrtho(zoom * -hx, zoom * hx, zoom * -hy, zoom * hy, 0, 100);
        viewMatrix.setToLookAt(new Vector3(0, 0, -1), new Vector3(0, 1, 0));
        viewMatrix.translate(-position.x, -position.y, -position.z);

        viewProjectionMatrix.set(projectionMatrix);
        viewProjectionMatrix.mul(viewMatrix);
    }
}
