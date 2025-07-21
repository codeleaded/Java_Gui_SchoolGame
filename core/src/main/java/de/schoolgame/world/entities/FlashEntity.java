package de.schoolgame.world.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;
import de.schoolgame.world.WorldObject;

public class FlashEntity extends MovingEntity {
    private float stateTime;

	private boolean lookDir;

    public FlashEntity(Vec2f pos) {
        super(pos, new Vec2f(0.5f,0.5f));
        this.stateTime = 0.0f;

	    this.lookDir = false;

        this.velocity = new Vec2f(-1.0f,new Random().nextFloat(-1.0f,1.0f));
    }

    public static final int[] WALK_LUT = new int[]{0,1,2,3,4};

    public int getTexIndex(){
        float speed = velocity.len();
        
        float stateTime = this.stateTime;
        stateTime = stateTime - (float)Math.floor(stateTime);
        stateTime *= speed * 10.0f;
        stateTime = stateTime - (float)Math.floor(stateTime);
        
        return WALK_LUT[(int)(WALK_LUT.length * stateTime)];
    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;

        acceleration.y = 0.0f;

        super.update();

        velocity = velocity.clamp(MAX_GROUND_VELOCITY.neg(), MAX_GROUND_VELOCITY);
    }

    @Override
    public void onCollision(Direction type,Vec2i pos,WorldObject object) {
        //if (type == LEFT || type == RIGHT)  velocity.x *= -1.0f;
        //if (type == UP || type == DOWN)     velocity.y *= -1.0f;
        var world = GameState.INSTANCE.world;
        world.getEntities().remove(this);
    }

    public boolean onEntityCollision(Entity entity, Direction direction) {
        return false;
    }

    @Override
    public void render(Batch batch) {
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();

        SpriteSheet texture = state.assetManager.get("entities/flash/flash",SpriteSheet.class);
        int index = getTexIndex();

        batch.draw(
            texture.getRegions()[index],
            position.x * tileSize, position.y * tileSize,
            0.5f * tileSize,0.5f * tileSize,
            size.x * tileSize, size.y * tileSize,// * (1.0f / (1.0f - (6.0f / 32.0f)))
            1.0f,1.0f,
            (float)(Math.atan2(velocity.y,velocity.x) * 180.0f)
        );
    }
}
