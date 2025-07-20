package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.schoolgame.primitives.Direction;
import static de.schoolgame.primitives.Direction.DOWN;
import static de.schoolgame.primitives.Direction.LEFT;
import static de.schoolgame.primitives.Direction.NONE;
import static de.schoolgame.primitives.Direction.RIGHT;
import static de.schoolgame.primitives.Direction.UP;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;
import de.schoolgame.world.WorldObject;

public class PlayerEntity extends MovingEntity {
    public static float COYOTE_TIME = 0.2f;

    private float stateTime;
    private float coyote;

	private int power;
    private int coins;

	private boolean lookDir;
    private boolean slideDir;
	private boolean reverse;
    private boolean stamp;
    private boolean onWall;
    private boolean onGround;
    private boolean onJump;
    private boolean dead;
    private boolean godmode;

    public PlayerEntity(Vec2f pos) {
        super(pos, new Vec2f(0.95f, 0.95f));
        this.stateTime = 0.0f;
        this.coyote = 0.0f;

        this.coins = 0;
	    this.power = 0;
	    this.lookDir = false;
	    this.stamp = false;
	    this.reverse = false;
        this.onGround = false;
        this.onJump = false;
        this.slideDir = false;
        this.godmode = false;
    }

    public void setJump(boolean jump) {
        this.onJump = jump;
    }
    public void setStamp(boolean stamp) {
        this.stamp = stamp;
    }
    public void setPower(int power) {
        switch (power) {
            case 0:
                size = new Vec2f(0.95f, 0.95f);
                break;
            case 1:
            case 2:
                size = new Vec2f(0.95f, 1.9f);
                break;
            default:
                throw new IllegalArgumentException("Invalid power level: " + power);
        }
        this.power = power;
    }
    public void setCoins(int coins) {
        this.coins = coins;
    }
    public void setGodmode(boolean godmode) {
        this.godmode = godmode;
    }

    public boolean getDead() { return dead; }
    public int getCoins() { return this.coins; }
    public int getPower() { return this.power; }
    public boolean getGodmode() { return this.godmode; }

    public void checkKill() {
        Vec2i worldSize = GameState.INSTANCE.world.getSize();

        if(getDead() && (position.y < -1.0f || position.y > worldSize.y || position.x < -1.0f || position.x > worldSize.x)) {
            position = GameState.INSTANCE.world.getSpawn().toVec2f().add(new Vec2f(0.0f,0.001f));
            velocity = new Vec2f(0.0f,0.0f);
            dead = false;
            setPower(0);
        }
    }

    public void kill() {
        if (godmode) return;
        if(getDead()) return;

        this.dead = true;
        velocity.y = 8.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);
    }

    public void cancelMovement(Direction direction) {
        if (godmode) {
            switch (direction) {
                case LEFT:
                case RIGHT:
                    velocity.x = 0f;
                    break;
                case UP:
                case DOWN:
                    velocity.y = 0f;
            }
        }else{
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
    }

    public boolean move(Direction direction) {
        if (direction == NONE) return false;
        if (godmode) {
            switch (direction) {
                case UP -> velocity.y = 10f;
                case DOWN -> velocity.y = -10f;
                case LEFT -> {
                    velocity.x = -10f;
                    lookDir = false;
                }
                case RIGHT -> {
                    velocity.x = 10f;
                    lookDir = true;
                }
            }
            return true;
        }
        switch (direction) {
            case UP -> {
                setJump(true);
                if (onWall) {
                    velocity.x = 4.0f * (slideDir ? 1.0f : -1.0f);
                    velocity.y = 8.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);
                }
                if (onGround || stateTime - coyote < COYOTE_TIME) {
                    velocity.y = 12.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);
                    this.coyote = 0.0f;
                }
            }
            case DOWN -> {
                setStamp(true);
                if (velocity.y>0) velocity.y = -8;
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

    public Rect getTexRect(){
        Rect r = new Rect();
	    r.size = new Vec2f( 1.0f,1.0f );

        reverse = (velocity.x<0.0f && acceleration.x>0.0f) || (velocity.x>0.0f && acceleration.x<0.0f);

	    if(getDead())				    r.pos = new Vec2f( 2.0f,1.0f );
	    else if(!onGround){
	    	if(!stamp)		        r.pos = new Vec2f( 2.0f,0.0f );
	    	else 				    r.pos = new Vec2f( 1.0f,1.0f );
	    }else if(stamp){
	    	if(velocity.x==0.0f)	r.pos = new Vec2f( 1.0f,1.0f );
	    	else 				    r.pos = new Vec2f( 0.0f,1.0f );
	    }
	    else if(reverse)		    r.pos = new Vec2f( 3.0f,0.0f );
        else if(velocity.y!=0.0f){
            float stateTime = this.stateTime;

            stateTime = stateTime - (float)Math.floor(stateTime);
            stateTime *= Math.abs(velocity.y) * 0.65f;
            stateTime = stateTime - (float)Math.floor(stateTime);

            r.pos = new Vec2f( 4.0f + (int)(4.0f * stateTime),1.0f );
        }else if(velocity.x==0.0f)	r.pos = new Vec2f( 7.0f,0.0f );
	    else{
            float stateTime = this.stateTime;
            stateTime = stateTime - (float)Math.floor(stateTime);
	    	stateTime *= Math.abs(velocity.x) * 0.65f;
	    	stateTime = stateTime - (float)Math.floor(stateTime);

	    	r.pos = new Vec2f( 4.0f + (int)(3.0f * stateTime),0.0f );
	    }

	    if(power==1)		r.pos.y = 2.0f + r.pos.y * 2.0f;
	    else if(power==2)	r.pos.y = 6.0f + r.pos.y * 2.0f;

	    if(power>0)         r.size.y *= 2.0f;
	    if(!lookDir)  r.pos.x = 15.0f - r.pos.x;

        r.pos = r.pos.div(new Vec2f(16.0f,12.0f));
        r.size = r.size.div(new Vec2f(16.0f,12.0f));
	    return r;
    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;

        checkKill();

        onGround = false;
        onWall = false;

        if(getGodmode())    acceleration.y = 0.0f;
        else                acceleration.y = GRAVITY;

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

        if (object == WorldObject.SPIKE) {
            kill();
        }

        if (object == WorldObject.BRICK) {
            if ((type == UP && velocity.y < 0.0f) || (type == DOWN && velocity.y > 0.0f)){
                var world = GameState.INSTANCE.world;
                world.addAt(pos,WorldObject.NONE);
            }
        }
        if (object == WorldObject.QUESTMARK) {
            if (type == DOWN && GRAVITY < 0.0f){
                var world = GameState.INSTANCE.world;
                world.addAt(pos,WorldObject.OPENQUESTMARK);
                world.addAt(pos.add(0,1),WorldObject.FIREFLOWER);
            }
            if (type == UP && GRAVITY > 0.0f){
                var world = GameState.INSTANCE.world;
                world.addAt(pos,WorldObject.OPENQUESTMARK);
                world.addAt(pos.add(0,-1),WorldObject.FIREFLOWER);
            }
        }

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
                coyote = stateTime;
            }
        }
        if ((type == UP && GRAVITY < 0.0f) || (type == DOWN && GRAVITY > 0.0f)) {
            onGround = true;
            coyote = stateTime;
        }
    }

    public boolean onEntityCollision(Entity entity, Direction direction) {
        if (getDead()) return false;

        if (entity instanceof CoinEntity) {
            coins += 1;
            return true;
        }
        if (entity instanceof Fireflower) {
            setPower(2);
            return true;
        }

        if (entity instanceof RoamerEntity) {
            if ((direction == UP && GRAVITY < 0.0f) || (direction == DOWN && GRAVITY > 0.0f)) {
                onGround = true;
                move(Direction.UP);
                return true;
            }
            kill();
        }

        return false;
    }

    @Override
    public void render(Batch batch) {
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();

        Texture texture = state.assetManager.get("entities/player/mario_atlas", Texture.class);
        Rect r = getTexRect();

        float yS = r.pos.y + r.size.y;
        float yE = r.pos.y;
        if(MovingEntity.GRAVITY > 0.0f){
            final float temp = yS;
            yS = yE;
            yE = temp;
        }

        batch.draw(texture,
            position.x * tileSize, position.y * tileSize,
            size.x * tileSize, size.y * tileSize,
            r.pos.x + r.size.x,yS,r.pos.x,yE
        );
    }
}
