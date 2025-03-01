package de.schoolgame.render;

import com.badlogic.gdx.graphics.Texture;

public class Animation {
    public enum Mode {
        Single, Loop
    }

    private final Texture[] frames;
    private final Mode mode;
    private float delay;

    public Animation(float delay, Texture[] frames) {
        this(delay, frames, Mode.Loop);
    }

    public Animation(float delay, Texture[] frames, Mode mode) {
        this.mode = mode;
        this.frames = frames;
        this.delay = delay;
    }

    public Texture currentFrame(float stateTime) {
        int frameIndex = (int) (stateTime / delay);

        frameIndex = switch (mode) {
            case Single -> Math.min(frames.length - 1, frameIndex);
            case Loop -> frameIndex % frames.length;
        };

        return frames[frameIndex];
    }

    public void dispose() {
        for (Texture frame : frames) {
            frame.dispose();
        }
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }
}
