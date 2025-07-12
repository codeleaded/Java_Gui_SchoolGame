package de.schoolgame.render.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
    public enum Mode {
        Single, Loop, Bounce
    }

    private final TextureRegion[] frames;
    private final Mode mode;
    private final float delay;

    public Animation(float delay, TextureRegion[] frames) {
        this(delay, frames, Mode.Loop);
    }

    public Animation(float delay, TextureRegion[] frames, Mode mode) {
        this.mode = mode;
        this.frames = frames;
        this.delay = delay;
    }

    public TextureRegion currentFrame(float stateTime) {
        int frameIndex = (int) (stateTime / delay);

        frameIndex = switch (mode) {
            case Single -> Math.min(frames.length - 1, frameIndex);
            case Loop -> frameIndex % frames.length;
            case Bounce -> {
                int mod = frameIndex % (frames.length * 2);
                yield mod < frames.length ? mod : frames.length - 1 - (mod - frames.length);
            }
        };

        return frames[frameIndex];
    }
}
