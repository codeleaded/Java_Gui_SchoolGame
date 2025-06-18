package de.schoolgame.render;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class AssetManager {
    private final HashMap<String, Object> assets = new HashMap<>();

    public <T> T get(String path, Class<T> type) {
        if (!path.startsWith("./")) path = "./" + path;
        if (!assets.containsKey(path)) throw new IllegalArgumentException("Texture not loaded: " + path);
        return type.cast(assets.get(path));
    }

    public void load(String path, Class<?> type) {
        if (assets.containsKey(path)) return;
        if (type == null) throw new IllegalArgumentException("Type must not be null");

        if (type == Texture.class) {
            Texture texture = new Texture(path);
            assets.put(path, texture);
            return;
        }
        throw new IllegalStateException("No Asset loader for: " + type);
    }
}
