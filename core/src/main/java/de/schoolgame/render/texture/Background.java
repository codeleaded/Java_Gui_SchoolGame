package de.schoolgame.render.texture;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;

public class Background {
    private final SpriteSheet spriteSheet;
    private final float[] speeds;
    private final Vec2f spriteSize;

    public Background(SpriteSheet spriteSheet, float[] speeds) {
        this.spriteSheet = spriteSheet;
        this.speeds = speeds;

        if (speeds.length != spriteSheet.regions.length) {
            throw new RuntimeException("Invalid sprite sheet length");
        }

        this.spriteSize = new Vec2i(spriteSheet.getRegions()[0].getRegionWidth(),
            spriteSheet.getRegions()[0].getRegionHeight()).toVec2f();
    }

    public void render(Batch batch) {
        var state = GameState.INSTANCE;


        int worldSize = state.world.getSize().x * state.world.getTileSize();
        int repeat = worldSize / state.camera.viewSize.x;

        for (int i = 0; i < speeds.length; i++) {
            Vec2f size = spriteSize.mul(speeds[i] + 1);
            Vec2f pos = state.camera.position.toVec2f().mul(speeds[i]);
            for (int j = -1; j < repeat; j++) {
                float xOffset = j * size.x;
                batch.draw(spriteSheet.getRegions()[i], pos.x + xOffset, 0, size.x, size.y);
            }
        }
    }
}
