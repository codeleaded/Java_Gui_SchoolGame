package de.schoolgame.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.schoolgame.world.Entity;

public class Player extends Entity {
    private static final Texture playerTexture = new Texture(Gdx.files.internal("entities/player/player.png"));
    public Vector2 velocity;
    public boolean jumping;

    public Player() {
        super(new Vector2(320, 180));
        velocity = new Vector2(0, 0);
    }

    @Override
    public void update() {
        position.x += velocity.x * Gdx.graphics.getDeltaTime();
        position.y += velocity.y * Gdx.graphics.getDeltaTime();
        if (position.y <= 0) {
            velocity.y = 0;
            jumping = false;
            position.y = 0;
        } else {
            velocity.y -= 100 * Gdx.graphics.getDeltaTime();
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

}
