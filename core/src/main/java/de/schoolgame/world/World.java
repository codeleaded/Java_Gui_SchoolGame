package de.schoolgame.world;

import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class World {
    private final Tile[][] tiles;
    private final List<Entity> entities;

    private Vec2i spawn;
    private final Vec2i size;
    private final int tileSize;

    public World() {
        this(new Tile[100][32], new Vec2i(100, 32), 32, new Vec2i(1, 1));

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 32; y++) {
                tiles[x][y] = Tile.NONE;
            }
        }
    }

    public World(Tile[][] tiles, Vec2i size, int tileSize, Vec2i spawn) {
        this.tiles = tiles;
        this.size = size;
        this.tileSize = tileSize;
        this.entities = new ArrayList<>();
        this.spawn = spawn;

        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                var tile = tiles[x][y];
                if (tile == null) {
                    continue;
                }
                var e = tile.createEntity(new Vec2f(x, y));
                if (e != null) {
                    entities.add(e);
                }
            }
        }
    }

    public void dispose() {
        for (Entity e : entities) {
            e.dispose();
        }
    }

    public Tile at(Vec2i pos) {
        if(pos.x < 0.0f || pos.x >= tiles.length)       return Tile.NONE;
        if(pos.y < 0.0f || pos.y >= tiles[pos.x].length)    return Tile.NONE;
        return tiles[pos.x][pos.y];
    }

    public void addAt(Vec2i pos, Tile tile) {
        tiles[pos.x][pos.y] = tile;
        var e = tile.createEntity(pos.toVec2f());
        if (e != null) {
            entities.add(e);
        }
    }

    public void removeAt(Vec2i pos) {
        tiles[pos.x][pos.y] = Tile.NONE;
        entities.removeIf(e -> e.position.toVec2i().equals(pos));
    }

    public Collection<Entity> getEntities() {
        return entities;
    }

    public Vec2i getSize() {
        return size;
    }

    public int getTileSize() {
        return tileSize;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Vec2i getSpawn() {
        return spawn;
    }

    public void setSpawn(Vec2i spawn) {
        this.spawn = spawn;
    }
}
