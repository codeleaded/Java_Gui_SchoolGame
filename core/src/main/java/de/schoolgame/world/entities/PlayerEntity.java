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
import de.schoolgame.state.GameState;

public class PlayerEntity extends MovingEntity {
    private long start;
	private int power;
	private boolean lookDir;
	private boolean stamp;
	private boolean reverse;
	private boolean dead;
    private boolean onGround;
    private boolean onJump;

    public PlayerEntity(Vec2f pos) {
        super(pos, new Vec2f(0.95f, 0.95f));
        this.start = 0L;
	    this.power = 0;
	    this.lookDir = false;
	    this.stamp = false;
	    this.reverse = false;
	    this.dead = false;
        this.onGround = false;
        this.onJump = false;
    }

    public void setJump(boolean jump) {
        this.onJump = jump;
    }
    public void setStamp(boolean stamp) {
        this.stamp = stamp;
    }
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean GetDead() {
        return this.dead;
    }

    public boolean move(Direction direction) {
        return switch (direction) {
            case UP:
                if (onGround) {
                    velocity.y = 8;
                    yield true;
                }
            case DOWN:
                if (velocity.y>0) {
                    velocity.y = -8;
                    yield true;
                }
            case LEFT:
                acceleration.x = -10;
                lookDir = false;
                yield true;
            case RIGHT:
                acceleration.x = 10;
                lookDir = true;
                yield true;
            case NONE:
                acceleration.x = 0;
            default: yield false;
        };
    }

    public Rect getTexRect(){
        Rect r = new Rect();
	    r.size = new Vec2f( 1.0f,1.0f );

        reverse = (velocity.x<0.0f && acceleration.x>0.0f) || (velocity.x>0.0f && acceleration.x<0.0f);
            
	    if(dead)				    r.pos = new Vec2f( 2.0f,1.0f );
	    else if(!onGround){
	    	if(!stamp)		        r.pos = new Vec2f( 2.0f,0.0f );
	    	else 				    r.pos = new Vec2f( 1.0f,1.0f );
	    }else if(stamp){
	    	if(velocity.x==0.0f)	r.pos = new Vec2f( 1.0f,1.0f );
	    	else 				    r.pos = new Vec2f( 0.0f,1.0f );
	    }
	    else if(reverse)		    r.pos = new Vec2f( 3.0f,0.0f );
	    else if(velocity.x==0.0f)	r.pos = new Vec2f( 7.0f,0.0f );
	    else{
	    	long ld = System.nanoTime() - start;
            float d = (float)ld / 1000_000_000.0f; 
            
            d = d - (float)Math.floor(d);
	    	d *= (float)Math.abs(velocity.x) * 0.65f;
	    	d = d - (float)Math.floor(d);
        
	    	r.pos = new Vec2f( 4.0f + (int)(3.0f * d),0.0f );
	    }

	    if(power==1)		r.pos.y = 2.0f + r.pos.y * 2.0f;
	    else if(power==2)	r.pos.y = 6.0f + r.pos.y * 2.0f;

	    if(power>0)         r.size.y *= 2.0f;
	    if(lookDir==false)  r.pos.x = 15.0f - r.pos.x;

        r.pos = r.pos.div(new Vec2f(16.0f,12.0f));
        r.size = r.size.div(new Vec2f(16.0f,12.0f));
	    return r;
    }

    @Override
    public void update() {
        onGround = false;
        super.update();

        float friction = AIR_FRICTION;
        if (onGround) friction += GROUND_FRICTION;

        float sign = velocity.x > 0 ? 1 : -1;
        friction *= -sign * Gdx.graphics.getDeltaTime();
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

        var state = GameState.INSTANCE;
        // Delete Coin
        state.world.getEntities().removeIf(e ->
            e instanceof CoinEntity &&
                getRect().overlap(new Rect(e.getPosition(), new Vec2f(0.9f, 0.9f)))
        );
    }

    @Override
    void onCollision(Direction type) {
        if (type == UP && velocity.y < 0.0f) velocity.y = 0.0f;
        if (type == DOWN && velocity.y > 0.0f) velocity.y = 0.0f;
        if (type == LEFT || type == RIGHT) velocity.x = 0.0f;
        if (type == UP) onGround = true;
    }

    @Override
    public void render(Batch batch) {
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();
        Texture texture = state.assetManager.get("entities/player/mario_atlas", Texture.class);
        
        Rect r = getTexRect();

        batch.draw(texture,
            position.x * tileSize, position.y * tileSize,
            size.x * tileSize, size.y * tileSize,
            r.pos.x + r.size.x,r.pos.y + r.size.y,r.pos.x,r.pos.y
        );
    }
}
