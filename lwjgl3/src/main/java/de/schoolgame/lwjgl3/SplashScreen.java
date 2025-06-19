package de.schoolgame.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import de.schoolgame.LoadingTask;
import de.schoolgame.Main;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.Animation;
import de.schoolgame.render.texture.SpriteSheet;

public class SplashScreen extends ApplicationAdapter {
    public final Vec2i size;
    private final int scale;

    private Texture loadingTexture;
    private Animation loadingAnimation;
    private float timeState;

    private ShapeRenderer shapeRenderer;
    private Batch batch;
    private BitmapFont font;

    private Lwjgl3Window window;

    private LoadingTask loadingTask;
    private float nextTime;

    public SplashScreen(int screenHeight) {
        float screenWidth = (float) screenHeight * (16f / 9f);
        size = new Vec2i((int) screenWidth, screenHeight).div(3);
        scale = screenHeight / 1080;
    }

    @Override
    public void create() {
        loadingTexture = new Texture("entities/coin/coin.png");
        var spriteSheet = new SpriteSheet(loadingTexture, new Vec2i(32, 32), 50);
        loadingAnimation = new Animation(0.03f, spriteSheet.getRegions());
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(scale);
        window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        loadingTask = new LoadingTask();
    }

    @Override
    public void render() {
        timeState += Gdx.graphics.getDeltaTime();
        if (nextTime < timeState) {
            loadingTask.update();
            nextTime = timeState + (1f / loadingTask.getInitialTasks());
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        int coin_size = size.y - 16;
        int bar_height = 16;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.rect(0, 0, size.x, size.y, Color.BLACK, Color.CORAL, Color.BLUE, Color.FOREST);

        shapeRenderer.setColor(0.15f, 0.15f, 0.2f, 1.0f);
        shapeRenderer.rect(0, 0, size.x, bar_height);

        shapeRenderer.setColor(1.0f, 1.0f, 0.95f, 1.0f);
        shapeRenderer.rect(0, 0, size.x * loadingTask.getProgress(), bar_height);

        shapeRenderer.end();
        batch.begin();

        batch.draw(loadingAnimation.currentFrame(timeState),
            (float) ((size.x / 2) - (coin_size / 2)),
            (float) ((size.y / 2) - (coin_size / 2)) + bar_height,
            coin_size, coin_size);

        font.draw(batch, loadingTask.getStatus(), 10 , bar_height + font.getLineHeight());

        batch.end();

        if (loadingTask.isFinished()) {
            Lwjgl3Application app = (Lwjgl3Application) Gdx.app;
            Lwjgl3ApplicationConfiguration config = Lwjgl3Launcher.getDefaultConfiguration();

            app.newWindow(new Main(), config);
            window.closeWindow();
        }
    }

    @Override
    public void dispose() {
        loadingTexture.dispose();
    }
}
