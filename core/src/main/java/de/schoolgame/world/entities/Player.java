package de.schoolgame.world.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.schoolgame.state.GameState;
import de.schoolgame.utils.primitives.Rect;
import de.schoolgame.utils.primitives.Vec2f;
import de.schoolgame.world.Entity;
import de.schoolgame.world.Tile;

public class Player extends Entity {
    private static final Texture playerTexture = new Texture(Gdx.files.internal("entities/player/player.png"));
    public Vec2f length;
    public Vec2f velocity;
    public Vec2f acceleration;
    public boolean jumpable;

    public Player() {
        super(new Vec2f(320, 80));

        length = new Vec2f(1.0f, 1.0f);
        velocity = new Vec2f(0.0f, 0.0f);
        acceleration = new Vec2f(0.0f, -5.0f);
    }

    Rect getRect(){
        return new Rect(position,length,velocity);
    }

    @Override
    public void update() {
        velocity = velocity.add(acceleration.scl(Gdx.graphics.getDeltaTime()));
        position = position.add(velocity.scl(Gdx.graphics.getDeltaTime()));

        if (position.y <= 0.0f) {
            velocity.y = 0.0f;
            position.y = 0.0f;
            jumpable = true;
        }

        var state = GameState.INSTANCE;
        Rect myrect = getRect();

        ArrayList<Rect> rects = new ArrayList<>();

		for(float x = -2.0f*length.x;x<2*length.x;x+=1.0f) {
			for(float y = -2*length.y;y<2*length.y;y+=1.0f) {
				int sx = (int)(position.x + x);
				int sy = (int)(position.y + y);

                if(sy>=0 && sy<state.world.getHeight() && sx>=0 && sx<state.world.getWidth()) {
					Tile b = state.world.at(sx,sy);
					if(b!=Tile.NONE) {
                        rects.add(new Rect(new Vec2f(sx, sy), new Vec2f(1.0f, 1.0f)));
					}
				}
			}
		}

        jumpable = false;

        rects.sort((r1, r2) -> {
            float d1 = r1.p.sub(myrect.p).len();
            float d2 = r2.p.sub(myrect.p).len();
            return Float.compare(d1, d2);
        });

        for (Rect rect : rects) {
            int type = myrect.StaticCollisionSolver(rect);
			if(type!=Rect.NONE) {
				if(type==Rect.TOP) jumpable = true;
			}
		}

        position = myrect.p;
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
