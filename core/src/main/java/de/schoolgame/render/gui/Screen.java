package de.schoolgame.render.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.Camera;
import de.schoolgame.render.gui.widgets.TextureButtonWidget;
import de.schoolgame.state.GameState;

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

        //DONT TOUCH - Concurrent Modification Exceptions Ahead!
        for (int i = 0; i < widgets.size(); i++) {
            widgets.get(i).render(batch, shapeRenderer);
        }
    }

    public boolean onClick(Vec2i clickPos) {
        //DONT TOUCH - Concurrent Modification Exceptions Ahead!
        for (int i = 0; i < widgets.size(); i++) {
            Widget widget = widgets.get(widgets.size() - 1 - i);
            if (widget.getRect().contains(clickPos)) {
                return widget.onClick();
            }
        }
        return false;
    }

    protected void addBackButton() {
        Vec2i size = new Vec2i(32, 32);
        Vec2i pos = camera.viewSize.sub(size);
        TextureButtonWidget button = new TextureButtonWidget(pos, size, 6,
            () -> GameState.INSTANCE.setState(GameState.GameStateType.MAIN_MENU));
        widgets.add(button);
    }

    public void dispose() {

    }
}
