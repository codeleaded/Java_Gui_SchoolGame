package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.schoolgame.primitives.Vec2f;
import de.schoolgame.render.texture.Animation;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;

public class KloEntity extends Entity {
    private float stateTime;

    public KloEntity(Vec2f position) {
        super(position, new Vec2f(1.0f, 1.0f));
        stateTime = 0f;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        var state = GameState.INSTANCE;
        Animation animation = state.assetManager.get("entities/klo/klo", Animation.class);

        batch.draw(animation.currentFrame(stateTime), this.getPixelPosition().x, this.getPixelPosition().y);
    }
}
