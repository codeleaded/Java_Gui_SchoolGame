package de.schoolgame.render;

import de.schoolgame.primitives.Vec2f;

import java.util.Random;

public class Sound {
    private final Random rand = new Random();
    private final com.badlogic.gdx.audio.Sound sound;

    private final Vec2f pitch;

    public Sound(com.badlogic.gdx.audio.Sound sound, Vec2f pitchRange) {
        this.sound = sound;
        this.pitch = pitchRange;
    }

    public void play() {
        play(1.0f);
    }

    public void play(float volume) {
        sound.play(volume, getPitch(), 0);
    }

    private float getPitch() {
        if (pitch.x < pitch.y) {
            return rand.nextFloat(pitch.x, pitch.y);
        } else {
            return pitch.x;
        }
    }
}
