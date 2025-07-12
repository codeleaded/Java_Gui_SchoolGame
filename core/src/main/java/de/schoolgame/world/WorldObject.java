package de.schoolgame.world;

import com.badlogic.gdx.graphics.Color;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.world.entities.CoinEntity;
import de.schoolgame.world.entities.Fireflower;
import de.schoolgame.world.entities.RoamerEntity;
import de.schoolgame.world.tiles.*;

public enum WorldObject {
    NONE(null),
    TEST_TILE(new Test()),
    GRASS(new Grass()),
    COIN(CoinEntity.class),
    ROAMER(RoamerEntity.class),
    FIREFLOWER(Fireflower.class),
    SPIKE(new Spike()),
    WORLD_BORDER(null),
    TRACK(new Track()),
    LABORATROYTABLE(new Laboratorytable()),
    TABLE(new Table()),
    HURDE(new Hurde()),
    CORRIDOR(new Corridor()),
    QUESTMARK(new QuestMark()),
    BRICK(new Brick())
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

    public Color getEntityColor() {
        return switch (this) {
            case ROAMER -> Color.BROWN;
            case COIN -> Color.YELLOW;
            case FIREFLOWER -> Color.RED;
            default -> Color.WHITE;
        };
    }
}
