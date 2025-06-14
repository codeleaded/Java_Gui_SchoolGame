package de.schoolgame.render.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.schoolgame.primitives.Vec2i;

public class SpriteSheet {
    protected final TextureRegion[] regions;

    public SpriteSheet(Texture texture, Vec2i spriteSize, int spriteCount) {
        regions = new TextureRegion[spriteCount];
        int x = 0, y = 0;
        for (int i = 0; i < spriteCount; i++) {
            regions[i] = new TextureRegion(texture, x, y, spriteSize.x, spriteSize.y);

            x += spriteSize.x;
            if (x  >= texture.getWidth()) {
                x = 0;
                y += spriteSize.y;
            }
        }
    }

    public TextureRegion[] getRegions() {
        return regions;
    }
}
