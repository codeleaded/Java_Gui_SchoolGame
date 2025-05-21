package de.schoolgame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.schoolgame.utils.FileUtils;
import de.schoolgame.utils.primitives.Vec2f;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class World {
    private final Tile[][] tiles;
    private final List<Entity> entities;

    private final int width, height, tileSize;

    public World(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        try(InputStream stream = file.read()) {
            byte[] bytes = new byte[4];

            if (stream.read(bytes) != 4) {
                throw new RuntimeException("Could not read world File! 1");
            }
            width = FileUtils.intFromBytes(bytes);

            if (stream.read(bytes) != 4) {
                throw new RuntimeException("Could not read world File! 2");
            }
            height = FileUtils.intFromBytes(bytes);

            if (stream.read(bytes) != 4) {
                throw new RuntimeException("Could not read world File! 3");
            }
            tileSize = FileUtils.intFromBytes(bytes);

            if (stream.available() != width * height) {
                throw new RuntimeException("Could not read world File! 4");
            }

            tiles = new Tile[width][height];
            entities = new ArrayList<>();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    var tile = Tile.values()[stream.read()];
                    tiles[x][y] = tile;
                    var e = tile.createEntity(new Vec2f(x, y));
                    if (e != null) {
                        entities.add(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] serialize() {
        byte[] result = new byte[(width * height) + (3 * 4)];
        byte[] w = FileUtils.bytesFromInt(width);
        System.arraycopy(w, 0, result, 0, 4);

        byte[] h = FileUtils.bytesFromInt(height);
        System.arraycopy(h, 0, result, 4, 4);

        byte[] s = FileUtils.bytesFromInt(tileSize);
        System.arraycopy(s, 0, result, 8, 4);

        int i = 3 * 4;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result[i] = tiles[x][y].index();
                i++;
            }
        }

        return result;
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
            entities.add(e);
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
}
