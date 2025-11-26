package de.schoolgame.render.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.schoolgame.primitives.Vec2i;

import java.util.stream.IntStream;

/**
 * 47-Tile Tileset
 */
public class TileSet extends SpriteSheet {
    private static final byte TOP = 0b0000_0001;
    private static final byte TOP_RIGHT = 0b0000_0010;
    private static final byte RIGHT = 0b0000_0100;
    private static final byte BOTTOM_RIGHT = 0b0000_1000;
    private static final byte BOTTOM = 0b0001_0000;
    private static final byte BOTTOM_LEFT = 0b0010_0000;
    private static final byte LEFT = 0b0100_0000;
    private static final byte TOP_LEFT = (byte) 0b1000_0000;

    private static final byte NONE = 0; //No Connection
    public static final byte INVALID = TOP_RIGHT; //TOP_RIGHT is not valid on its own

    private static final byte[] LAYOUT = new byte[] {
        BOTTOM,
        RIGHT | BOTTOM,
        RIGHT | BOTTOM | LEFT,
        BOTTOM | LEFT,
        TOP | RIGHT | BOTTOM | LEFT | TOP_LEFT,
        RIGHT | BOTTOM_RIGHT | BOTTOM | LEFT,
        RIGHT | BOTTOM | BOTTOM_LEFT | LEFT,
        TOP | TOP_RIGHT | RIGHT | BOTTOM | LEFT,
        RIGHT | BOTTOM_RIGHT | BOTTOM,
        TOP | RIGHT | BOTTOM_RIGHT | BOTTOM | BOTTOM_LEFT | LEFT,
        RIGHT | BOTTOM_RIGHT | BOTTOM | BOTTOM_LEFT | LEFT,
        BOTTOM | BOTTOM_LEFT | LEFT,
        TOP | BOTTOM,
        TOP | RIGHT | BOTTOM,
        TOP | RIGHT | BOTTOM | LEFT,
        TOP | BOTTOM | LEFT,
        TOP | RIGHT | BOTTOM_RIGHT | BOTTOM,
        TOP | TOP_RIGHT | RIGHT | BOTTOM_RIGHT | BOTTOM | BOTTOM_LEFT | LEFT,
        TOP | RIGHT | BOTTOM_RIGHT | BOTTOM | BOTTOM_LEFT | LEFT | TOP_LEFT,
        TOP | BOTTOM | BOTTOM_LEFT | LEFT,
        TOP | TOP_RIGHT | RIGHT | BOTTOM_RIGHT | BOTTOM,
        TOP | TOP_RIGHT | RIGHT | BOTTOM | BOTTOM_LEFT | LEFT,
        INVALID, // No Tile Saved here
        TOP | RIGHT | BOTTOM | BOTTOM_LEFT | LEFT | TOP_LEFT,
        TOP,
        TOP | RIGHT,
        TOP | RIGHT | LEFT,
        TOP | LEFT,
        TOP | TOP_RIGHT | RIGHT | BOTTOM,
        TOP | TOP_RIGHT | RIGHT | BOTTOM_RIGHT | BOTTOM | LEFT | TOP_LEFT,
        TOP | TOP_RIGHT | RIGHT | BOTTOM | BOTTOM_LEFT | LEFT | TOP_LEFT,
        TOP | BOTTOM | LEFT | TOP_LEFT,
        TOP | TOP_RIGHT | RIGHT | BOTTOM_RIGHT | BOTTOM | LEFT,
        TOP | TOP_RIGHT | RIGHT | BOTTOM_RIGHT | BOTTOM | BOTTOM_LEFT | LEFT | TOP_LEFT,
        TOP | RIGHT | BOTTOM_RIGHT | BOTTOM | LEFT | TOP_LEFT,
        TOP | BOTTOM | BOTTOM_LEFT | LEFT | TOP_LEFT,
        NONE,
        RIGHT,
        RIGHT | LEFT,
        LEFT,
        TOP | RIGHT | BOTTOM | BOTTOM_LEFT | LEFT,
        TOP | TOP_RIGHT | RIGHT | LEFT,
        TOP | RIGHT | LEFT | TOP_LEFT,
        TOP | RIGHT | BOTTOM_RIGHT | BOTTOM | LEFT,
        TOP | TOP_RIGHT | RIGHT,
        TOP | TOP_RIGHT | RIGHT | LEFT | TOP_LEFT,
        TOP | TOP_RIGHT | RIGHT | BOTTOM | LEFT | TOP_LEFT,
        TOP | LEFT | TOP_LEFT
    };

    private static final byte[] LUT = buildLUT();

    public TileSet(Texture texture, Vec2i spriteSize) {
        super(texture, spriteSize, 48);
    }

    /**
     * @param connections Bits (LSB-MSB):<br/>
     *                    Top, Top-right, Right, Bottom-right,
     *                    Bottom, Bottom-left, Left, Top-left
     */
    public TextureRegion getTextureRegion(byte connections) {
        byte i = LUT[128 + connections];
        return regions[i];
    }

    private static byte[] buildLUT() {
        byte[] lut = new byte[256];
        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            byte val = hash((byte) i);
            byte res = (byte) IntStream.range(0, 48).filter(ind -> LAYOUT[ind] == val).findFirst().orElseThrow();
            lut[128 + i] = res;
        }
        return lut;
    }

    /**
     * @param c connections
     * @return byte for looking up an index in {@link TileSet#LAYOUT}
     */
    private static byte hash(byte c) {
        if (!(getBit(c, 0) && getBit(c, 2))) {
            c = zeroBit(c, 1);
        }
        if (!(getBit(c, 2) && getBit(c, 4))) {
            c = zeroBit(c, 3);
        }
        if (!(getBit(c, 4) && getBit(c, 6))) {
            c = zeroBit(c, 5);
        }
        if (!(getBit(c, 6) && getBit(c, 0))) {
            c = zeroBit(c, 7);
        }
        return c;
    }

    private static boolean getBit(byte b, int index) {
        if (index < 0 || index > 7) throw new IllegalArgumentException("Index must be 0-7");
        return (b & (1 << index)) != 0;
    }

    private static byte zeroBit(byte b, int index) {
        if (index < 0 || index > 7) throw new IllegalArgumentException("Index must be 0-7");
        b &= (byte) ~(1 << index);
        return b;
    }
}
