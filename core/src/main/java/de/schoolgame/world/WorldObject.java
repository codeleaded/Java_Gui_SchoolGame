package de.schoolgame.world;

import de.schoolgame.primitives.Vec2f;
import de.schoolgame.world.entities.CoinEntity;
import de.schoolgame.world.entities.RoamerEntity;
import de.schoolgame.world.tiles.GrassTile;
import de.schoolgame.world.tiles.SpikeTile;
import de.schoolgame.world.tiles.TestTile;

public enum WorldObject {
    NONE(null),
    TEST_TILE(new TestTile()),
    GRASS(new GrassTile()),
    COIN(CoinEntity.class),
    ROAMER(RoamerEntity.class),
    SPIKE(new SpikeTile()),
    WORLD_BORDER(null)
    ;

    private final Object object;

    WorldObject(Object object) {
        this.object = object;
    }

    public boolean isTile() {
        return object instanceof Tile;
    }

    public boolean isEntity() {
        return object instanceof Class<?> cls && Entity.class.isAssignableFrom(cls);
    }

    public Tile getTile() {
        if (!isTile()) {
            return null;
        }

        return (Tile) object;
    }


    private Class<? extends Entity> getEntityClass() {
        if (!isEntity()) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Class<? extends Entity> entityClass = (Class<? extends Entity>) object;

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
