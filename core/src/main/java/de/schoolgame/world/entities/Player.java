package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.schoolgame.world.Entity;

public class Player extends Entity {
    private PlayerState playerState;
    private static final Texture playerTexture = new Texture(Gdx.files.internal("entities/player/player.png"));

    public Player() {
        super(new Vector2(320, 180));
        playerState = PlayerState.IDLE;
    }

    @Override
    public void update() {
        switch (playerState) {
            case IDLE:
                break;
            case MOVING_LEFT:
                position.x -= 500 * Gdx.graphics.getDeltaTime();
                break;
            case MOVING_RIGHT:
                position.x += 500 * Gdx.graphics.getDeltaTime();
                break;
        }
    }

    @Override
    public void render(Batch batch) {
        batch.draw(playerTexture, position.x, position.y);
    }

    @Override
    public void dispose() {
        playerTexture.dispose();
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public enum PlayerState {
        IDLE,
        MOVING_LEFT,
        MOVING_RIGHT,
    }
}
