package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.Font;
import de.schoolgame.state.GameState;
import de.schoolgame.world.WorldObject;

import java.util.Random;

public class PointsEntity extends MovingEntity {
    public int value;
    public float angle;

    public PointsEntity(Vec2f position) {
        super(position, new Vec2f(0.95f, 0.95f));
        velocity.x = new Random().nextFloat(-1.0f,1.0f);
        velocity.y = 10f;
        value = 0;
        angle = 0.0f;
    }

    @Override
    public void update() {
        this.acceleration = new Vec2f(0.0f, GRAVITY);
        super.update();

        angle += 180 * velocity.x * Gdx.graphics.getDeltaTime();
    }

    @Override
    void onCollision(Direction type,Vec2i pos,WorldObject object) {
        var world = GameState.INSTANCE.world;
        if (type == Direction.UP) world.getEntities().remove(this);
        if (type == Direction.LEFT || type == Direction.RIGHT) this.velocity.x *= -0.5f;
        if (type == Direction.DOWN) this.velocity.y *= -0.5f;
    }

    @Override
    public void render(Batch batch) {
        Font font = GameState.INSTANCE.assetManager.get("gui/font/aseprite_font", Font.class);
        font.draw(batch, ""+value,getPixelPosition().toVec2i(),2,angle);
    }
}
