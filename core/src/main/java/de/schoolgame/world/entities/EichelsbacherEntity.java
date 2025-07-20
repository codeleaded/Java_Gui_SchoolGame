package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.schoolgame.primitives.Direction;
import static de.schoolgame.primitives.Direction.DOWN;
import static de.schoolgame.primitives.Direction.LEFT;
import static de.schoolgame.primitives.Direction.NONE;
import static de.schoolgame.primitives.Direction.RIGHT;
import static de.schoolgame.primitives.Direction.UP;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;
import de.schoolgame.world.WorldObject;

public class EichelsbacherEntity extends MovingEntity {
    private float stateTime;

	private boolean lookDir;
    private boolean slideDir;
	private boolean reverse;
    private boolean stamp;
    private boolean onWall;
    private boolean onGround;
    private boolean onJump;
    private boolean dead;

    public EichelsbacherEntity(Vec2f pos) {
        super(pos, new Vec2f(0.95f * 2.0f, 1.9f * 2.0f));
        this.stateTime = 0.0f;

	    this.lookDir = false;
	    this.stamp = false;
	    this.reverse = false;
        this.onGround = false;
        this.onJump = false;
        this.slideDir = false;

        move(Direction.LEFT);
    }

    public void setJump(boolean jump) {
        this.onJump = jump;
    }
    public void setStamp(boolean stamp) {
        this.stamp = stamp;
    }
    
    public boolean getDead() { return dead; }

    public void checkKill() {
        Vec2i worldSize = GameState.INSTANCE.world.getSize();

        if(getDead() && (position.y < -1.0f || position.y > worldSize.y || position.x < -1.0f || position.x > worldSize.x)) {
            var state = GameState.INSTANCE;
            state.world.getEntities().remove(this);
        }
    }

    public void kill() {
        if(getDead()) return;

        this.dead = true;
        velocity.y = 8.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);
    }

    public void cancelMovement(Direction direction) {
        switch (direction) {
            case LEFT:
            case RIGHT:
                acceleration.x = 0;
                break;
            case UP:
                setJump(false);
            case DOWN:
                setStamp(false);
        }
    }

    public boolean move(Direction direction) {
        if (direction == NONE) return false;
        switch (direction) {
            case UP -> {
                setJump(true);
                if (onWall) {
                    velocity.x = 4.0f * (slideDir ? 1.0f : -1.0f);
                    velocity.y = 8.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);
                }
                if (onGround) {
                    velocity.y = 12.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);
                }
            }
            case DOWN -> {
                setStamp(true);
                if (velocity.y * (GRAVITY > 0.0f ? -1.0f : 1.0f)>0)
                    velocity.y = 8.0f * (GRAVITY > 0.0f ? 1.0f : -1.0f);
            }
            case LEFT -> {
                acceleration.x = -10;
                lookDir = false;
            }
            case RIGHT -> {
                acceleration.x = 10;
                lookDir = true;
            }
        }
        return true;
    }

    public static final int[] WALK_LUT = new int[]{6, 8, 5, 8, 6, 7, 4, 7};

    public int getTexIndex(){
        reverse = (velocity.x<0.0f && acceleration.x>0.0f) || (velocity.x>0.0f && acceleration.x<0.0f);

        if(getDead())				return 2;
        else if(!onGround){
            if(!stamp)		        return 3;
            else 				    return 1;
        }else if(stamp){
            if(velocity.x==0.0f)	return 1;
            else 				    return 1;
        }
        else if(reverse)		    return 1;
        else if(velocity.y!=0.0f){
            float stateTime = this.stateTime;

            stateTime = stateTime - (float)Math.floor(stateTime);
            stateTime *= Math.abs(velocity.y) * 0.4f;
            stateTime = stateTime - (float)Math.floor(stateTime);

            return WALK_LUT[(int)(WALK_LUT.length * stateTime)];
        }else if(velocity.x==0.0f)	return 0;
        else{
            float stateTime = this.stateTime;
            stateTime = stateTime - (float)Math.floor(stateTime);
            stateTime *= Math.abs(velocity.x) * 0.4f;
            stateTime = stateTime - (float)Math.floor(stateTime);

            return WALK_LUT[(int)(WALK_LUT.length * stateTime)];
        }

        //if(power==1)		return 0;
        //else if(power==2)	return 0;
        //if(power>0)         r.size.y *= 2.0f;
    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;

        checkKill();

        onGround = false;
        onWall = false;

        acceleration.y = GRAVITY;

        super.update();

        float friction = AIR_FRICTION;
        if (onGround) friction += GROUND_FRICTION;

        float sign = velocity.x > 0 ? 1 : -1;
        friction *= -sign * delta;
        velocity.x += friction;

        // Zero crossing point
        if (sign != (velocity.x > 0 ? 1 : -1)) velocity.x = 0;

        if(onJump) this.acceleration.y = GRAVITY * 0.33f;
        else       this.acceleration.y = GRAVITY;

        if (onGround) {
            velocity = velocity.clamp(MAX_GROUND_VELOCITY.neg(), MAX_GROUND_VELOCITY);
        } else {
            velocity = velocity.clamp(MAX_AIR_VELOCITY.neg(), MAX_AIR_VELOCITY);
        }
    }

    @Override
    public void onCollision(Direction type,Vec2i pos,WorldObject object) {
        if (getDead()) return;

        if (type == UP && velocity.y < 0.0f) velocity.y = 0.0f;
        if (type == DOWN && velocity.y > 0.0f) velocity.y = 0.0f;

        if (type == LEFT || type == RIGHT){
            if(object==WorldObject.WORLD_BORDER)
                velocity.x = 0.0f;
            else {
                onWall = true;
                onGround = true;
                velocity.x = 0.0f;
                slideDir = type == RIGHT;
            }

            move(type);
        }
        if ((type == UP && GRAVITY < 0.0f) || (type == DOWN && GRAVITY > 0.0f)) {
            onGround = true;
        }
    }

    public boolean onEntityCollision(Entity entity, Direction direction) {
        if (getDead()) return false;
        return false;
    }

    @Override
    public void render(Batch batch) {
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();

        SpriteSheet texture = state.assetManager.get("entities/eichelsbacher/eichelsbacher",SpriteSheet.class);
        int index = getTexIndex() + ((lookDir && MovingEntity.GRAVITY < 0.0f) || (!lookDir && MovingEntity.GRAVITY > 0.0f) ? 0 : 9);

        batch.draw(texture.getRegions()[index],
            position.x * tileSize, position.y * tileSize,
            0.5f * tileSize,0.5f * tileSize,
            size.x * tileSize, size.y * tileSize,// * (1.0f / (1.0f - (6.0f / 32.0f)))
            1.0f,1.0f,
            MovingEntity.GRAVITY < 0.0f ? 0.0f : 180.0f
        );
    }
}
