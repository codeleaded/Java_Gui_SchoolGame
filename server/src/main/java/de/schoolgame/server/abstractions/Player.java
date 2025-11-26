package de.schoolgame.server.abstractions;

import de.schoolgame.primitives.Vec2f;

import java.util.UUID;

public class Player {
    UUID uuid;
    String name;
    String ip;
    int style;

    Vec2f position;
    Vec2f velocity;
    Vec2f acceleration;
}
