package de.schoolgame.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import de.schoolgame.primitives.Recti;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.Animation;
import de.schoolgame.render.texture.Font;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.render.texture.TileSet;
import de.schoolgame.utils.AssetUtils;
import org.tomlj.TomlParseResult;

import java.util.HashMap;
import java.util.Optional;

public class AssetManager {
    private final HashMap<String, Object> assets = new HashMap<>();

    public <T> T get(String path, Class<T> type) {
        path = path + "." + AssetUtils.getType(type);
        if (!path.startsWith("assets/")) path = "assets/" + path;
        T asset = type.cast(assets.get(path));
        if (asset == null) {
            throw new IllegalArgumentException("Asset not loaded: " + path);
        }
        return asset;
    }

    public void load(String path) throws IllegalStateException, NullPointerException {
        if (path == null) throw new IllegalArgumentException("Path must not be null");
        if (!path.startsWith("assets/")) path = "assets/" + path;

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

            int count = AssetUtils.getInt(asset, "spritesheet.count", -1);
            Optional<Recti[]> opt = AssetUtils.getSpriteSizes(asset);

            SpriteSheet spriteSheet;
            if (opt.isPresent()) {
                Recti[] sizes = opt.get();
                if (sizes.length != count && count != -1) throw new IllegalStateException("SpriteSheet sizes don't match");
                spriteSheet = new SpriteSheet(texture, sizes);
            } else {
                if (count == -1) throw new IllegalStateException("SpriteSheet size not found");
                Vec2i size = AssetUtils.getSpriteSize(asset, new Vec2i(32, 32));
                spriteSheet = new SpriteSheet(texture, size, count);
            }

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
            load(path, "texture");
            Texture texture = get(pathWithoutExtension, Texture.class);

            Font font = new Font(texture);
            assets.put(name, font);
            return;
        } else if (typeClass == Sound.class) {
            FileHandle handle = Gdx.files.internal(pathWithoutExtension + ".wav");

            Vec2f pitch = AssetUtils.getSoundPitch(asset);
            Sound sound = new Sound(Gdx.audio.newSound(handle), pitch);
            assets.put(name, sound);
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
