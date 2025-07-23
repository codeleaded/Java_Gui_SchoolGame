package de.schoolgame.world;

import com.badlogic.gdx.graphics.Color;
import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.world.entities.*;
import de.schoolgame.world.tiles.AnimatedTile;
import de.schoolgame.world.tiles.SimpleTile;
import de.schoolgame.world.tiles.TileSetTile;

public enum WorldObject {
    NONE(null),
    TEST_TILE(new SimpleTile("tiles/test/test")),
    GRASS(new TileSetTile("tiles/grass/grass")),
    COIN(CoinEntity.class),
    TESLA(new AnimatedTile("tiles/tesla/tesla")),
    KLO(new AnimatedTile("tiles/klo/klo")),
    ROAMER(RoamerEntity.class),
    FRIEDRICH(FriedrichEntity.class),
    POINTS(PointsEntity.class),
    KOENIG(KoenigEntity.class),
    EICHELSBACHER(EichelsbacherEntity.class),
    FIREFLOWER(Fireflower.class),
    FLASH(FlashEntity.class),
    SPIKE(new TileSetTile("tiles/spikes/spikes")),
    REDSPIKE(new TileSetTile("tiles/redspikes/redspikes")),
    WORLD_BORDER(null),
    TRACK(new TileSetTile("tiles/track/track")),
    LABORATROYTABLE(new TileSetTile(Direction.UP,"tiles/laboratorytable/laboratorytable")),
    TABLE(new TileSetTile(Direction.UP,"tiles/table/table")),
    HURDE(new SimpleTile("tiles/hurde/hurde")),
    CORRIDOR(new TileSetTile("tiles/corridor/corridor")),
    RECORRIDOR(new TileSetTile("tiles/redcorridor/redcorridor")),
    CONCRETE(new TileSetTile("tiles/concrete/concrete")),
    BATH(new TileSetTile("tiles/bath/bath")),
    QUESTMARK(new AnimatedTile("tiles/questmark/questmark")),
    OPENQUESTMARK(new SimpleTile("tiles/openquestmark/openquestmark")),
    PODEST(new SimpleTile(Direction.UP,"tiles/podest/podest")),
    BRICK(new SimpleTile("tiles/brick/brick")),
    CHEMIKALIEN(new AnimatedTile("tiles/chemikalien/chemikalien")),
    COMPUTER(ComputerEntity.class),
    BUNSENBRENNER(new AnimatedTile("tiles/bunsenbrenner/bunsenbrenner")),
    POTION(PotionEntity.class),
    CABLE(new AnimatedTile("tiles/cable/cable"))
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
