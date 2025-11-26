package de.schoolgame.world.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.state.GameState;
import de.schoolgame.world.WorldObject;

import java.util.Random;

public class BrickAnimationEntity extends MovingEntity {
    public int value;
    public float angle;
    private final int id;

    public BrickAnimationEntity(int id,Vec2f position, Vec2f velocity) {
        super(position, new Vec2f(1.0f, 1.0f));
        //this.velocity.x = new Random().nextFloat(-3.0f,3.0f);
        //this.velocity.y = new Random().nextFloat(3.0f,8.0f);

        this.velocity = velocity.cpy();
        this.velocity = this.velocity.add(
            new Random().nextFloat(-3.0f,3.0f),
            new Random().nextFloat(-3.0f,3.0f)
        );

        this.value = 0;
        this.angle = 0.0f;
        this.id = id;
    }

    @Override
    public void update() {
        this.acceleration = new Vec2f(0.0f, GRAVITY);
        super.update();
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
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();

        SpriteSheet texture = state.assetManager.get("tiles/brickanimation/brickanimation",SpriteSheet.class);

        batch.draw(
            texture.getRegions()[id],
            position.x * tileSize, position.y * tileSize,
            0.5f * tileSize,0.5f * tileSize,
            size.x * tileSize, size.y * tileSize,// * (1.0f / (1.0f - (6.0f / 32.0f)))
            1.0f,1.0f,
            (float)(Math.atan2(velocity.y,velocity.x) / Math.PI * 180.0f + 90)
        );
    }
}
