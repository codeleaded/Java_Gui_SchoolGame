package de.schoolgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.texture.Animation;
import de.schoolgame.render.texture.SpriteSheet;
import de.schoolgame.render.texture.TileSet;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

public class AssetUtils {
    public static TomlParseResult getAsset(String path) {
        String asset = Gdx.files.internal(path).readString();
        return Toml.parse(asset);
    }

    public static int getInt(TomlParseResult asset, String key, int defaultValue) {
        return (int) asset.getLong(key, () -> defaultValue);
    }

    public static float getFloat(TomlParseResult asset, String key, float defaultValue) {
        return (float) asset.getDouble(key, () -> defaultValue);
    }

    public static String getString(TomlParseResult asset, String key, String defaultValue) {
        return asset.getString(key, () -> defaultValue);
    }

    public static Class<?> getClass(TomlParseResult asset) {
        return switch (getString(asset, "type", "null")) {
            case "texture" -> Texture.class;
            case "spritesheet" -> SpriteSheet.class;
            case "animation" -> Animation.class;
            case "tileset" -> TileSet.class;
            default -> throw new GdxRuntimeException("Unknown asset type: " + getString(asset, "type", "null"));
        };
    }

    public static String getType(Class<?> type) {
        if (type == Texture.class) return "texture";
        if (type == SpriteSheet.class) return "spritesheet";
        if (type == Animation.class) return "animation";
        if (type == TileSet.class) return "tileset";
        throw new GdxRuntimeException("Unknown asset type: " + type.getName());
    }

    public static Vec2i getSpriteSize(TomlParseResult asset, Vec2i defaultSize) {
        int width = (int) asset.getLong("sprite.width", () -> defaultSize.x);
        int height = (int) asset.getLong("sprite.height", () -> defaultSize.y);
        return new Vec2i(width, height);
    }
}
