package de.schoolgame.utils;

import de.schoolgame.primitives.Vec2i;
import de.schoolgame.world.WorldObject;

import java.io.Serializable;

public record Save(WorldObject[][] worldObjects, int tileSize, Vec2i spawn) implements Serializable {
}
