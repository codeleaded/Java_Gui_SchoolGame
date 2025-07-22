package de.schoolgame.world;

import com.badlogic.gdx.graphics.Color;

import de.schoolgame.primitives.Vec2f;
import de.schoolgame.world.entities.*;
import de.schoolgame.world.tiles.*;

public enum WorldObject {
    NONE(null),
    TEST_TILE(new Test()),
    GRASS(new Grass()),
    COIN(CoinEntity.class),
    TESLA(TeslaEntity.class),
    KLO(KloEntity.class),
    ROAMER(RoamerEntity.class),
    FRIEDRICH(FriedrichEntity.class),
    POINTS(PointsEntity.class),
    KOENIG(KoenigEntity.class),
    EICHELSBACHER(EichelsbacherEntity.class),
    FIREFLOWER(Fireflower.class),
    FLASH(FlashEntity.class),
    SPIKE(new Spike()),
    REDSPIKE(new Redspike()),
    WORLD_BORDER(null),
    TRACK(new Track()),
    LABORATROYTABLE(new Laboratorytable()),
    TABLE(new Table()),
    HURDE(new Hurde()),
    CORRIDOR(new Corridor()),
    RECORRIDOR(new Redcorridor()),
    CONCRETE(new Concrete()),
    BATH(new Bath()),
    QUESTMARK(new QuestMark()),
    OPENQUESTMARK(new OpenQuestMark()),
    PODEST(new Podest()),
    BRICK(new Brick()),
    CHEMIKALIEN(ChemikalienEntity.class),
    COMPUTER(ComputerEntity.class),
    BUNSENBRENNER(BunsenbrennerEntity.class),
    POTION(PotionEntity.class),
    CABLE(CableEntity.class)
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
