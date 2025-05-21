package de.schoolgame.world;

import de.schoolgame.state.GameState;
import de.schoolgame.utils.primitives.Vec2f;
import de.schoolgame.world.entities.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class World {
    private final Tile[][] tiles;
    private final List<Entity> entities;

    private final int width, height, tileSize;

    public World() {
        this(new Tile[100][32], 100, 32, 32);

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 32; y++) {
                tiles[x][y] = Tile.NONE;
            }
        }
    }

    public World(Tile[][] tiles, int width, int height, int tileSize) {
        this.tiles = tiles;
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.entities = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
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

    public Tile at(int x, int y) {
        if(x < 0.0f || x >= tiles.length)       return Tile.NONE;
        if(y < 0.0f || y >= tiles[x].length)    return Tile.NONE;
        return tiles[x][y];
    }

    public void addAt(int x, int y, Tile tile) {
        tiles[x][y] = tile;
        var e = tile.createEntity(new Vec2f(x, y));
        if (e != null) {
            if (e instanceof Player) {
                GameState.INSTANCE.player = (Player) e;
            } else {
                entities.add(e);
            }
        }
    }

    public void removeAt(int x, int y) {
        tiles[x][y] = Tile.NONE;
        entities.removeIf(e -> (int) e.position.x == x && (int) e.position.y == y);
    }

    public Collection<Entity> getEntities() {
        return entities;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileSize;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
