package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.schoolgame.primitives.Direction;
import static de.schoolgame.primitives.Direction.DOWN;
import static de.schoolgame.primitives.Direction.LEFT;
import static de.schoolgame.primitives.Direction.RIGHT;
import static de.schoolgame.primitives.Direction.UP;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.Animation;
import de.schoolgame.state.GameState;
import de.schoolgame.world.WorldObject;

public class Fireflower extends MovingEntity {
    private float stateTime;

    public Fireflower(Vec2f position) {
        super(position, new Vec2f(0.95f, 0.95f));
        velocity.x = 0f;
    }

    @Override
    public void update() {
        this.acceleration = new Vec2f(0.0f, GRAVITY);
        super.update();
    }

    @Override
    void onCollision(Direction type,Vec2i pos,WorldObject object) {
        if (type == RIGHT || type == LEFT){
            velocity.x = -velocity.x;
        }

        if (type == UP && velocity.y < 0.0f)    velocity.y = 0.0f;
        if (type == DOWN && velocity.y > 0.0f)  velocity.y = 0.0f;
    }

    @Override
    public void render(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();
        Animation animation = state.assetManager.get("entities/fireflower/fireflower", Animation.class);

        batch.draw(
            animation.currentFrame(stateTime),
            this.getPixelPosition().x,this.getPixelPosition().y,
            0.5f * tileSize,0.5f * tileSize,
            tileSize,
            tileSize,
            1.0f,1.0f,
            MovingEntity.GRAVITY < 0.0f ? 0.0f : 180.0f
        );
    }
}
