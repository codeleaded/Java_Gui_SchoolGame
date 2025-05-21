package de.schoolgame.world;

import com.badlogic.gdx.graphics.Texture;
import de.schoolgame.utils.primitives.Vec2f;
import de.schoolgame.world.entities.Coin;
import de.schoolgame.world.entities.Player;

public enum Tile {
    NONE(null),
    TEST_TILE(new Texture("tiles/test.png")),
    COIN(Coin.class),
    PLAYER(Player.class),
    ;

    private final Object textureOrEntity;

    Tile(Object textureOrEntity) {
        this.textureOrEntity = textureOrEntity;
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

    public boolean isDrawable() {
        return textureOrEntity instanceof Texture;
    }

    public Texture getTexture() {
        if (this.isDrawable()) {
            return (Texture) textureOrEntity;
        } else {
            return null;
        }
    }

    private Class<? extends Entity> getEntityClass() {
        if (!(textureOrEntity instanceof Class<?> cls)) {
            return null;
        }

        if (!Entity.class.isAssignableFrom(cls)) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Class<? extends Entity> entityClass = (Class<? extends Entity>) cls;

        return entityClass;
    }

    public Entity createEntity(Vec2f pos) {
        var entity_class = getEntityClass();
        if (entity_class == null) {
            return null;
        }
        try {
            var constructor = entity_class.getConstructor(Vec2f.class);
            return constructor.newInstance(pos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
