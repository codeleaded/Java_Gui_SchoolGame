package de.schoolgame.render;

import com.badlogic.gdx.graphics.Texture;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.Animation;
import de.schoolgame.render.texture.Font;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.render.texture.TileSet;
import de.schoolgame.utils.AssetUtils;
import org.tomlj.TomlParseResult;

import java.util.HashMap;

public class AssetManager {
    private final HashMap<String, Object> assets = new HashMap<>();

    public <T> T get(String path, Class<T> type) {
        path = path + "." + AssetUtils.getType(type);
        if (!path.startsWith("./")) path = "./" + path;
        if (!assets.containsKey(path)) throw new IllegalArgumentException("Asset not loaded: " + path);
        return type.cast(assets.get(path));
    }

    public void load(String path) throws IllegalStateException, NullPointerException {
        if (path == null) throw new IllegalArgumentException("Path must not be null");

        TomlParseResult asset = AssetUtils.getAsset(path);
        String type = AssetUtils.getTypeString(asset);

        load(path, type);
    }

    private void load(String path, String type) throws IllegalStateException, NullPointerException {
        if (type == null) throw new IllegalArgumentException("Type must not be null");
        Class<?> typeClass = AssetUtils.getClass(type);

        TomlParseResult asset = AssetUtils.getAsset(path);
        String pathWithoutExtension = path.substring(0, path.lastIndexOf('.'));
        String name = pathWithoutExtension + "." + type;

        if (typeClass == Texture.class) {
            Texture texture = new Texture(pathWithoutExtension + ".png");
            assets.put(name, texture);
            return;
        } else if (typeClass == SpriteSheet.class) {
            load(path, "texture");
            Texture texture = get(pathWithoutExtension, Texture.class);

            int count = AssetUtils.getInt(asset, "spritesheet.count", 1);
            Vec2i size = AssetUtils.getSpriteSize(asset, new Vec2i(32, 32));

            SpriteSheet spriteSheet = new SpriteSheet(texture, size, count);
            assets.put(name, spriteSheet);
            return;
        } else if (typeClass == Animation.class) {
            load(path, "spritesheet");
            SpriteSheet spriteSheet = get(pathWithoutExtension, SpriteSheet.class);

            float frameDuration = AssetUtils.getFloat(asset, "animation.frameDuration", 0.03f);
            Animation.Mode animationType = Animation.Mode.valueOf(AssetUtils.getString(asset, "animation.mode", "Loop"));

            Animation animation = new Animation(frameDuration, spriteSheet.getRegions(), animationType);
            assets.put(name, animation);
            return;
        } else if (typeClass == TileSet.class) {
            load(path, "texture");
            Texture texture = get(pathWithoutExtension, Texture.class);

            Vec2i size = AssetUtils.getSpriteSize(asset, new Vec2i(32, 32));

            TileSet tileSet = new TileSet(texture, size);
            assets.put(name, tileSet);
            return;
        } else if (typeClass == Font.class) {
            load(path, "spritesheet");
            SpriteSheet spriteSheet = get(pathWithoutExtension, SpriteSheet.class);

            Font font = new Font(spriteSheet.getRegions());
            assets.put(name, font);
            return;
        }
        throw new IllegalStateException("No Asset loader for: " + type);
    }

    public void dispose() {
        assets.forEach((path, asset) -> {
            if (asset instanceof Texture texture) {
                texture.dispose();
            }
        });
    }
}
