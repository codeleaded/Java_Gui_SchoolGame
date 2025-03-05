package de.schoolgame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.schoolgame.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public class World {
    private Tile[][] tiles;
    //TODO entities

    private int width, height, tileSize;

    public World(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        try(InputStream stream = file.read()) {
            byte[] bytes = new byte[4];
            stream.read(bytes);
            width = FileUtils.intFromBytes(bytes);

            stream.read(bytes);
            height = FileUtils.intFromBytes(bytes);

            stream.read(bytes);
            tileSize = FileUtils.intFromBytes(bytes);

            tiles = new Tile[width][height];

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    tiles[x][y] = Tile.values()[stream.read()];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public Tile at(int x, int y) {
        return tiles[x][y];
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
