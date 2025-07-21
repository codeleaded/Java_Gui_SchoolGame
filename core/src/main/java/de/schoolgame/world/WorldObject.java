package de.schoolgame.world;

import com.badlogic.gdx.graphics.Color;

import de.schoolgame.primitives.Vec2f;
import de.schoolgame.world.entities.*;
import de.schoolgame.world.tiles.Brick;
import de.schoolgame.world.tiles.Corridor;
import de.schoolgame.world.tiles.Grass;
import de.schoolgame.world.tiles.Hurde;
import de.schoolgame.world.tiles.Laboratorytable;
import de.schoolgame.world.tiles.OpenQuestMark;
import de.schoolgame.world.tiles.QuestMark;
import de.schoolgame.world.tiles.Spike;
import de.schoolgame.world.tiles.Table;
import de.schoolgame.world.tiles.Test;
import de.schoolgame.world.tiles.Track;

public enum WorldObject {
    NONE(null),
    TEST_TILE(new Test()),
    GRASS(new Grass()),
    COIN(CoinEntity.class),
    TESLA(TeslaEntity.class),
    KLO(KloEntity.class),
    ROAMER(RoamerEntity.class),
    FRIEDRICH(FriedrichEntity.class),
    KOENIG(KoenigEntity.class),
    EICHELSBACHER(EichelsbacherEntity.class),
    FIREFLOWER(Fireflower.class),
    FLASH(FlashEntity.class),
    SPIKE(new Spike()),
    WORLD_BORDER(null),
    TRACK(new Track()),
    LABORATROYTABLE(new Laboratorytable()),
    TABLE(new Table()),
    HURDE(new Hurde()),
    CORRIDOR(new Corridor()),
    QUESTMARK(new QuestMark()),
    OPENQUESTMARK(new OpenQuestMark()),
    BRICK(new Brick()),
    CHEMIKALIEN(ChemikalienEntity.class)
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
