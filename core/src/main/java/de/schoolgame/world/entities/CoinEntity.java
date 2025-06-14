package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.Animation;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.world.Entity;

public class CoinEntity extends Entity {
    private final Texture texture;
    private final Animation coin;
    private float stateTime;

    public CoinEntity(Vec2f position) {
        super(position);

        texture = new Texture("entities/coin/coin.png");
        SpriteSheet spriteSheet = new SpriteSheet(texture, new Vec2i(32, 32), 50);
        coin = new Animation(0.03f, spriteSheet.getRegions());

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
        texture.dispose();
    }
}
