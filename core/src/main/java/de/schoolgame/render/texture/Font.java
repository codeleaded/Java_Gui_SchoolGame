package de.schoolgame.render.texture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;

public class Font {
    TextureRegion[] regions;

    public Font(TextureRegion[] regions) {
        this.regions = regions;

        TextureData data = regions[0].getTexture().getTextureData();
        data.prepare();
        Pixmap p = data.consumePixmap();

        for (TextureRegion region : regions) {
            Rect b = calculateBoundary(p, region);
            region.setRegion((int) b.pos.x, (int) b.pos.y, (int) b.size.x, (int) b.size.y);
        }
    }

    private static Rect calculateBoundary(Pixmap p, TextureRegion region) {
        int sx = region.getRegionX();
        int sy = region.getRegionY();
        int xw = sx + region.getRegionWidth();
        int yh = sy + region.getRegionHeight();

        boolean first = false;
        int fx = 0, fy = 0;
        int lx = 0, ly = 0;

        for (int x = sx; x < xw; x++) {
            for (int y = sy; y < yh; y++) {
                if (p.getPixel(x, y) != -1) {
                    lx = x;
                    ly = y;

                    if (!first) {
                        first = true;
                        fx = x;
                        fy = y;
                    }
                }
            }
        }

        return new Rect(new Vec2f(fx, fy), new Vec2f(lx - fx, ly - fy));
    }

    private int getIndex(char c) {
        int index = c - ' ';
        if (index >= 0 && index < regions.length) {
            return index;
        } else {
            return 0;
        }
    }

    public TextureRegion getTextureRegion(char c) {
        return regions[getIndex(c)];
    }

    public int getWidth(char c, int scale) {
        TextureRegion region = getTextureRegion(c);
        return region.getRegionWidth() * scale;
    }

    public int getWidth(String text, int scale) {
        int width = 0;
        for (char c : text.toCharArray()) {
            width += getWidth(c, scale);
            width += scale;
        }
        return width - scale;
    }

    public void draw(Batch b, String text, float x, float y, int scale) {
        int xOffset = 0;
        for (char c : text.toCharArray()) {
            TextureRegion region = getTextureRegion(c);
            b.draw(region, (x + xOffset), y, region.getRegionWidth() * scale, region.getRegionHeight() * scale);
            xOffset += region.getRegionWidth() * scale;
            xOffset += scale;
        }
    }
}
