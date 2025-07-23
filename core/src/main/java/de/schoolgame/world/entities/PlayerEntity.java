package de.schoolgame.world.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Affine2;

import de.schoolgame.network.packet.ScorePacket;
import de.schoolgame.primitives.Direction;
import static de.schoolgame.primitives.Direction.DOWN;
import static de.schoolgame.primitives.Direction.LEFT;
import static de.schoolgame.primitives.Direction.NONE;
import static de.schoolgame.primitives.Direction.RIGHT;
import static de.schoolgame.primitives.Direction.UP;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.Sound;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Entity;
import de.schoolgame.world.Score;
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
        super(pos, new Vec2f(0.9f, 0.9f * (58f / 32f)));
        this.stateTime = 0.0f;
        this.coyote = 0.0f;

        this.coins = 0;
	    this.power = 0;
	    this.lookDir = true;
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
            case 1:
            case 2:
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
        GameState.INSTANCE.debug.inputGodmode.set(godmode);
        
        if(godmode){
            velocity.x = 0.0f;
            velocity.y = 0.0f;
            acceleration.x = 0.0f;
            acceleration.y = 0.0f;
        }
    }
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean getDead() { return dead; }
    public int getCoins() { return this.coins; }
    public int getScore() { return GameState.INSTANCE.score; }
    public boolean getStamp() { return this.stamp; }
    public int getPower() { return this.power; }
    public boolean getGodmode() { return this.godmode; }
    public boolean getGround() { return this.onGround; }
    public boolean getWall() { return this.onWall; }

    public void setVelocityX(float vx){
        velocity.x = vx;
    }
    public void setAccelerationX(float ax){
        acceleration.x = ax;
    }

    public void addCoins(int coins) {
        if(GameState.INSTANCE.getState() == GameState.GameStateType.GAME){
            this.coins += coins;
        }
    }
    public void addScore(Vec2f pos,int value) {
        if(GameState.INSTANCE.getState() == GameState.GameStateType.GAME){
            GameState.INSTANCE.score += value;

            var world = GameState.INSTANCE.world;
            var e = (PointsEntity) WorldObject.POINTS.createEntity(pos);
            assert e != null;
            e.value = value;
            world.spawnEntity(pos,e);

            var s = GameState.INSTANCE.server;
            s.sendPacket(new ScorePacket(s.getUUID(), getScore()),true);
        }
    }
    public void addScore(int value) {
        addScore(position,value);
    }

    public void checkKill() {
        Vec2i worldSize = GameState.INSTANCE.world.getSize();

        if(getDead() && (position.y < -1.0f || position.y > worldSize.y || position.x < -1.0f || position.x > worldSize.x)) {
            position = GameState.INSTANCE.world.getSpawn().toVec2f().add(new Vec2f(0.0f,0.001f));
            velocity = new Vec2f(0.0f,0.0f);
            MovingEntity.GRAVITY *= (MovingEntity.GRAVITY < 0.0f ? 1.0f : -1.0f);

            lookDir = true;
            dead = false;
            setPower(0);
        }
    }

    public void kill() {
        if (godmode) return;
        if(getDead()) return;

        addScore(-Score.MP_DEATH);
        velocity.x = 0;
        acceleration.x = 0;

        this.dead = true;
        velocity.y = 8.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);

        Sound[] sounds = {
            GameState.INSTANCE.assetManager.get("audio/dead1/dead1", Sound.class),
            GameState.INSTANCE.assetManager.get("audio/dead3/dead3", Sound.class),
            GameState.INSTANCE.assetManager.get("audio/dead4/dead4", Sound.class)
        };

        sounds[new Random().nextInt(0,sounds.length)].play(2.0f);
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

    public void move(Direction direction) {
        if (direction == NONE) return;
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
            return;
        }
        switch (direction) {
            case UP -> {
                if(!dead){
                    setJump(true);
                    if (onGround) {
                        velocity.y = 14.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);
                        this.coyote = 0.0f;
                        
                        Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/jump/jump", Sound.class);
                        sound.play();
                    }else if (onWall) {
                        velocity.x = 4.0f * (slideDir ? 1.0f : -1.0f);
                        velocity.y = 8.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);
                        this.coyote = 0.0f;
                        
                        Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/jump/jump", Sound.class);
                        sound.play();
                    }else if (stateTime - coyote < COYOTE_TIME) {
                        velocity.y = 14.0f * (GRAVITY < 0.0f ? 1.0f : -1.0f);
                        this.coyote = 0.0f;
                        
                        Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/jump/jump", Sound.class);
                        sound.play();
                    }
                }
            }
            case DOWN -> {
                if(!dead){
                    setStamp(true);
                    if (velocity.y * (GRAVITY > 0.0f ? -1.0f : 1.0f)>0)
                        velocity.y = 8.0f * (GRAVITY > 0.0f ? 1.0f : -1.0f);
                }
            }
            case LEFT -> {
                if(!dead){
                    acceleration.x = -10;
                    lookDir = false;
                }
            }
            case RIGHT -> {
                if(!dead){
                    acceleration.x = 10;
                    lookDir = true;
                }
            }
        }
    }

    public static final int[] WALK_LUT = new int[]{6, 8, 5, 8, 6, 7, 4, 7};

    public int getTexIndex(){
        reverse = (velocity.x<0.0f && acceleration.x>0.0f) || (velocity.x>0.0f && acceleration.x<0.0f);

        if(getDead())				return 2;
        else if(!onGround && !onWall){
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
    public void onCollision(Direction type, Vec2i pos, WorldObject object) {
        if (getDead()) return;

        switch (object) {
            case SPIKE, REDSPIKE, CABLE, CHEMIKALIEN, BUNSENBRENNER, KLO, TESLA -> kill();
            case BRICK -> {
                if (type == DOWN && (GRAVITY < 0.0f || (stamp && Math.abs(velocity.y)>0.5f))){
                    var world = GameState.INSTANCE.world;
                    addScore(pos.toVec2f().add(0.0f,1.0f),Score.MP_BRICK);
                    world.addAt(pos,WorldObject.NONE);

                    for(int i = 0;i<11;i++){
                        var e = new BrickAnimationEntity(i,pos.toVec2f(),velocity);
                        world.spawnEntity(pos.toVec2f(),e);
                    }
                }
                if (type == UP && (GRAVITY > 0.0f || (stamp && Math.abs(velocity.y)>0.5f))){
                    var world = GameState.INSTANCE.world;
                    addScore(pos.toVec2f().add(0.0f,1.0f),Score.MP_BRICK);
                    world.addAt(pos,WorldObject.NONE);

                    for(int i = 0;i<11;i++){
                        var e = new BrickAnimationEntity(i,pos.toVec2f(),velocity);
                        world.spawnEntity(pos.toVec2f(),e);
                    }
                }
            }
            case QUESTMARK -> {
                if (type == DOWN && (GRAVITY < 0.0f || (stamp && Math.abs(velocity.y)>0.5f))){
                    var world = GameState.INSTANCE.world;
                    addScore(pos.toVec2f().add(0.0f,1.0f),Score.MP_QUESTMARK);
                    world.addAt(pos,WorldObject.OPENQUESTMARK);
                    world.addAt(pos.add(0,1),WorldObject.FIREFLOWER);
                }
                if (type == UP && (GRAVITY > 0.0f || (stamp && Math.abs(velocity.y)>0.5f))){
                    var world = GameState.INSTANCE.world;
                    addScore(pos.toVec2f().add(0.0f,1.0f),Score.MP_QUESTMARK);
                    world.addAt(pos,WorldObject.OPENQUESTMARK);
                    world.addAt(pos.add(0,-1),WorldObject.FIREFLOWER);
                }
            }

        }

        if (type == UP && velocity.y < 0.0f)    velocity.y = 0.0f;
        if (type == DOWN && velocity.y > 0.0f)  velocity.y = 0.0f;

        if (type == LEFT || type == RIGHT){
            if(object==WorldObject.WORLD_BORDER)
                velocity.x = 0.0f;
            else {
                onWall = true;
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

        switch (entity) {
            case CoinEntity ignored -> {
                addCoins(1);
                addScore(Score.MP_COIN);

                Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/coin/coin", Sound.class);
                sound.play();
                return true;
            }
            case Fireflower ignored -> {
                setPower(2);

                addScore(Score.MP_FIREFLOWER);

                Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/powerup/powerup", Sound.class);
                sound.play();
                return true;
            }
            case PotionEntity ignored -> {
                addScore(Score.MP_POTION);

                Sound sound = GameState.INSTANCE.assetManager.get("audio/upgrade/upgrade", Sound.class);
                sound.play();
                return true;
            }
            case RoamerEntity ignored -> {
                if ((direction == UP && GRAVITY < 0.0f) || (direction == DOWN && GRAVITY > 0.0f)) {
                    onGround = true;
                    move(UP);
                    addScore(Score.MP_KILL_ROAMER);
                    return true;
                } else {
                    kill();
                }
            }
            case FlashEntity ignored -> {
                MovingEntity.GRAVITY *= -1;
                return true;
            }
            case FriedrichEntity fe -> {
                if ((direction == UP && GRAVITY < 0.0f) || (direction == DOWN && GRAVITY > 0.0f)) {
                    onWall = true;
                    slideDir = false;
                    move(UP);

                    fe.lifes -= Math.sqrt(Math.abs(velocity.y));
                    
                    if(fe.lifes<=0.0f){
                        addScore(Score.MP_KILL_FRIEDRICH);
                        fe.kill();

                        Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/explosion/explosion", Sound.class);
                        sound.play();
                    }
                } else {
                    kill();
                }
            }
            case KoenigEntity ke -> {
                if (ke.getDead()) return false;
                if ((direction == UP && GRAVITY < 0.0f) || (direction == DOWN && GRAVITY > 0.0f)) {
                    onWall = true;
                    slideDir = false;
                    move(UP);

                    ke.lifes -= Math.sqrt(Math.abs(velocity.y));
                    
                    if(ke.lifes<=0.0f){
                        addScore(Score.MP_KILL_FRIEDRICH);
                        ke.kill();

                        Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/explosion/explosion", Sound.class);
                        sound.play();
                    }
                } else {
                    kill();
                }
            }
            case EichelsbacherEntity ee -> {
                if ((direction == UP && GRAVITY < 0.0f) || (direction == DOWN && GRAVITY > 0.0f)) {
                    onWall = true;
                    slideDir = false;
                    move(UP);

                    ee.lifes -= Math.sqrt(Math.abs(velocity.y));
                    
                    if(ee.lifes<=0.0f){
                        addScore(Score.MP_KILL_FRIEDRICH);
                        ee.kill();

                        Sound sound = GameState.INSTANCE.assetManager.get("audio/endleveldone/endleveldone", Sound.class);
                        sound.play();
                    }

                } else {
                    kill();
                }
            }
            default -> {}
        }

        return false;
    }

    @Override
    public void render(Batch batch) {
        var state = GameState.INSTANCE;
        int tileSize = state.world.getTileSize();

        int style = Math.clamp(state.playerStyle, 1, 7);
        SpriteSheet texture = state.assetManager.get("entities/player/player_" + style, SpriteSheet.class);
        int index = getTexIndex() + ((lookDir && MovingEntity.GRAVITY < 0.0f) || (!lookDir && MovingEntity.GRAVITY > 0.0f) ? 0 : 9);

        Vec2f rendersize = size.mul(1.1f,1.1f);

        Affine2 tf = new Affine2();
        tf.translate(position.x * tileSize, position.y * tileSize);
        tf.translate(0.5f * rendersize.x * tileSize,0.5f * rendersize.y * tileSize);
        tf.rotate(MovingEntity.GRAVITY < 0.0f ? 0.0f : 180.0f);
        tf.translate(-0.5f * rendersize.x * tileSize,-0.5f * rendersize.y * tileSize);

        batch.draw(
            texture.getRegions()[index],
            rendersize.x * tileSize, rendersize.y * tileSize,// * (1.0f / (1.0f - (6.0f / 32.0f)))
            tf
        );
    }
}
