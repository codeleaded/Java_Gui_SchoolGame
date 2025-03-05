package de.schoolgame.world;

import com.badlogic.gdx.graphics.Texture;

public enum Tile {
    NONE(null),
    TEST_TILE(new Texture("tiles/test.png")),
    ;

    public final Texture texture;

    Tile(Texture texture) {
        this.texture = texture;
    }

    public byte index() {
        byte i = 0;
        for (Tile t : Tile.values()) {
            if (t == this) {
                return i;
            }
            i++;
        }
        throw new RuntimeException("No matching tile found!");
    }
}
