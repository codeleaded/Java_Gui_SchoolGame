package de.schoolgame.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import de.schoolgame.state.GameState;

public class Camera {
    public Vector3 position;
    public float zoom;
    public int viewWidth, viewHeight;

    public Matrix4 projectionMatrix = new Matrix4();
    public Matrix4 viewMatrix = new Matrix4();
    public Matrix4 viewProjectionMatrix = new Matrix4();

    public Camera() {
        position = new Vector3(320, 180, 0);
        zoom = 1;
        viewWidth = 640;
        viewHeight = 360;
    }

    public Camera(float x, float y, float z) {
        position = new Vector3(x, y, z);
        zoom = 1;
        this.viewWidth = 640;
        this.viewHeight = 360;
    }

    public void update() {
        if (GameState.INSTANCE.rightMove) {
            position.x += 500 * Gdx.graphics.getDeltaTime();
        } else if (GameState.INSTANCE.leftMove) {
            position.x -= 500 * Gdx.graphics.getDeltaTime();
        }
        position.x = Math.max(position.x, 320);

        int hx = viewWidth / 2;
        int hy = viewHeight / 2;
        projectionMatrix.setToOrtho(zoom * -hx, zoom * hx, zoom * -hy, zoom * hy, 0, 100);
        viewMatrix.setToLookAt(new Vector3(0, 0, -1), new Vector3(0, 1, 0));
        viewMatrix.translate(-position.x, -position.y, -position.z);

        viewProjectionMatrix.set(projectionMatrix);
        viewProjectionMatrix.mul(viewMatrix);
    }
}
