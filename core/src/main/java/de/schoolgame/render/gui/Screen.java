package de.schoolgame.render.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.Camera;
import de.schoolgame.render.gui.widgets.ButtonWidget;

import java.util.ArrayList;

public abstract class Screen {
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;
    protected Camera camera;

    protected ArrayList<Widget> widgets;

    public Screen() {
        widgets = new ArrayList<>();
        camera = new Camera();
        camera.setGui();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    public void render() {
        camera.update();
        batch.setProjectionMatrix(camera.viewProjectionMatrix);
        shapeRenderer.setProjectionMatrix(camera.viewProjectionMatrix);

        widgets.forEach(w -> w.render(batch, shapeRenderer));
    }

    public boolean onClick(Vec2i clickPos) {
        boolean result = false;
        for (Widget widget : widgets) {
            if (widget.getRect().contains(clickPos)) {
                widget.onClick();
                if (widget instanceof ButtonWidget b) {
                    System.out.println(b.getText());
                }
                result = true;
            }
        }
        return result;
    }

    public void dispose() {

    }
}
