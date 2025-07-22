package de.schoolgame.render.texture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.schoolgame.primitives.Recti;
import de.schoolgame.primitives.Vec2i;

import java.util.ArrayList;
import java.util.Optional;

public class Font {
    TextureRegion[] regions;

    public Font(Texture texture) {
        TextureData data = texture.getTextureData();
        data.prepare();
        Pixmap p = data.consumePixmap();

        ArrayList<TextureRegion> regions = new ArrayList<>();
        regions.add(new TextureRegion(texture, 0, 0, 0, 0));
        regions.add(new TextureRegion(texture, 0, 0, 0, 0));

        Recti rect = new Recti();
        while (true) {
            Optional<Recti> opt = findGlyph(p, rect);
            if (opt.isEmpty()) break;
            Recti bounds = opt.get();
            regions.add(new TextureRegion(texture, bounds.pos.x, bounds.pos.y, bounds.size.x, bounds.size.y));
        }
        this.regions = regions.toArray(new TextureRegion[0]);
    }

    private Optional<Recti> findGlyph(Pixmap p, Recti rect) {
        int key = p.getPixel(0, 0);

        while (p.getPixel(rect.pos.x, rect.pos.y) == key) {
            rect.pos.x++;
            if (rect.pos.x >= p.getWidth()) {
                rect.pos.x = 0;
                rect.pos.y += rect.size.y;
                rect.size.y = 1;

                if (rect.pos.y >= p.getHeight()) {
                    return Optional.empty();
                }
            }
        }

        int pixel = p.getPixel(rect.pos.x, rect.pos.y);

        rect.size.x = 0;
        while ((rect.pos.x + rect.size.x < p.getWidth()) && (p.getPixel(rect.pos.x + rect.size.x, rect.pos.y) != key)) {
            rect.size.x++;
        }

        rect.size.y = 0;
        while ((rect.pos.y + rect.size.y < p.getHeight()) && (p.getPixel(rect.pos.x, rect.pos.y + rect.size.y) != key)) {
            rect.size.y++;
        }

        Recti res;
        if (pixel != 0xff0000ff)
            res = new Recti(rect.pos.cpy(), rect.size);
        else
            res = new Recti();

        rect.pos.x += rect.size.x;
        return Optional.of(res);
    }

    private int getIndex(char c) {
        int index = c - ' ' + 2;
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

    public int getHeight(int scale) {
        return scale * 7;
    }

    public void draw(Batch b, String text, Vec2i pos, int scale) {
        int xOffset = 0;
        for (char c : text.toCharArray()) {
            TextureRegion region = getTextureRegion(c);
            b.draw(region, (pos.x + xOffset), pos.y, region.getRegionWidth() * scale, region.getRegionHeight() * scale);
            xOffset += region.getRegionWidth() * scale;
            xOffset += scale;
        }
    }

    public void draw(Batch b, String text, Vec2i pos, int scale, float angle) {
        int xOffset = 0;
        for (char c : text.toCharArray()) {
            TextureRegion region = getTextureRegion(c);

            b.draw(
                region,
                (pos.x + xOffset),pos.y,
                0.0f,0.0f,
                region.getRegionWidth() * scale,region.getRegionHeight() * scale,
                1.0f,1.0f,
                angle
            );

            xOffset += region.getRegionWidth() * scale;
            xOffset += scale;
        }
    }
}
