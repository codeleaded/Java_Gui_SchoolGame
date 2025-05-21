package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.render.Animation;
import de.schoolgame.utils.primitives.Vec2f;
import de.schoolgame.world.Entity;

public class Coin extends Entity {
    private final Animation coin;
    private float stateTime;

    public Coin(Vec2f position) {
        super(position);

        Texture[] textures = new Texture[50];
        for (int i = 1; i < 51; i++) {
            textures[i - 1] = new Texture("coin/" + i + ".png");
        }
        coin = new Animation(0.03f, textures);

        stateTime = 0f;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        batch.draw(coin.currentFrame(stateTime), this.getPixelPosition().x, this.getPixelPosition().y);
    }

    @Override
    public void dispose() {
        coin.dispose();
    }
}
