package de.schoolgame.world;

import java.util.ArrayList;
import java.util.List;

import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.TileSet;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.Save;

public class World {
    private WorldObject[][] worldObjects;
    private final List<Entity> entities;

    private Vec2i spawn;
    private Vec2i size;
    private final int tileSize;

    private byte[][] connectionsCache;

    public World() {
        size = new Vec2i(100, 32);
        spawn = new Vec2i(1, 1);
        tileSize = 32;
        entities = new ArrayList<>();
        worldObjects = new WorldObject[size.x][size.y];

        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                worldObjects[x][y] = WorldObject.NONE;
            }
        }

        connectionsCache = new byte[size.x][size.y];
        updateConnections();
    }

    public World(Save s) {
        this(s.worldObjects(), new Vec2i(s.worldObjects().length, s.worldObjects()[0].length), s.tileSize(), s.spawn());
    }

    public World(WorldObject[][] worldObjects, Vec2i size, int tileSize, Vec2i spawn) {
        this.worldObjects = worldObjects;
        this.size = size;
        this.tileSize = tileSize;
        this.entities = new ArrayList<>();
        this.spawn = spawn;

        connectionsCache = new byte[size.x][size.y];
        updateConnections();
    }

    public void summonEntities() {
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                var tile = worldObjects[x][y];
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

    private boolean invalidPos(Vec2i pos) {
        return pos.x < 0 || pos.x >= size.x || pos.y < 0 || pos.y >= size.y;
    }

    public WorldObject at(Vec2i pos) {
        if(invalidPos(pos)) return WorldObject.NONE;
        return worldObjects[pos.x][pos.y];
    }

    public void updateConnectionsAt(Vec2i pos) {
        connectionsCache[pos.x][pos.y] = connections(pos);
        for (Vec2i p : pos.around()) {
            if(invalidPos(p)) continue;
            connectionsCache[p.x][p.y] = connections(p);
        }
    }

    public byte connectionsAt(Vec2i pos) {
        if(invalidPos(pos)) return TileSet.INVALID;
        return connectionsCache[pos.x][pos.y];
    }

    public void addAt(Vec2i pos, WorldObject worldObject) {
        if(invalidPos(pos)) return;
        this.worldObjects[pos.x][pos.y] = worldObject;
        if (GameState.INSTANCE.state != GameState.GameStateType.WORLD_EDITOR) {
            var e = worldObject.createEntity(pos.toVec2f());
            if (e != null) {
                entities.add(e);
            }
        }
        updateConnectionsAt(pos);
    }

    public void removeAt(Vec2i pos) {
        if(invalidPos(pos)) return;
        worldObjects[pos.x][pos.y] = WorldObject.NONE;
        entities.removeIf(e -> e.position.toVec2i().equals(pos));
        updateConnectionsAt(pos);
    }

    public void updateConnections() {
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                if (worldObjects[x][y].isTile()) {
                    connectionsCache[x][y] = connections(new Vec2i(x, y));
                } else {
                    connectionsCache[x][y] = TileSet.INVALID;
                }
            }
        }
    }

    private byte connections(Vec2i pos) {
        if(invalidPos(pos)) return TileSet.INVALID;
        
        WorldObject wo = at(pos);
        byte result = 0;
        Vec2i[] around = pos.around();
        for (int i = 0; i < 8; i++) {
            WorldObject woa = at(around[i]);
            result |= woa.isTile() && wo==woa ? (byte) (1 << i) : 0;
        }
        return result;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Vec2i getSize() {
        return size;
    }
    public void setSize(Vec2i newSize) {
        WorldObject[][] newWorldObjects = new WorldObject[newSize.x][newSize.y];

        for (int x = 0; x < newSize.x; x++) {
            for (int y = 0; y < newSize.y; y++) {
                if (x >= size.x || y >= size.y) {
                    newWorldObjects[x][y] = WorldObject.NONE;
                } else {
                    newWorldObjects[x][y] = worldObjects[x][y];
                }

            }
        }

        connectionsCache = new byte[newSize.x][newSize.y];
        worldObjects = newWorldObjects;
        size = newSize;
        updateConnections();
    }

    public int getTileSize() {
        return tileSize;
    }

    public WorldObject[][] getTiles() {
        return worldObjects;
    }

    public Vec2i getSpawn() {
        return spawn.clamp(new Vec2i(), size.sub(1, 1));
    }

    public void setSpawn(Vec2i spawn) {
        this.spawn = spawn;
    }
}
