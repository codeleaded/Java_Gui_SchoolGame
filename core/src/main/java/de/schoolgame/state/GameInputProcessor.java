package de.schoolgame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.L;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import com.badlogic.gdx.InputProcessor;

import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.utils.DebugUtils;

public class GameInputProcessor implements InputProcessor {
    private int lastMouseButton = Input.Buttons.LEFT;

    public void update() {
        var state = GameState.INSTANCE;

        var leftPressed = isKeyPressed(A) || isKeyPressed(LEFT);
        var rightPressed = isKeyPressed(D) || isKeyPressed(RIGHT);
        if (leftPressed != rightPressed) {
            if (leftPressed) {
                state.player.move(Direction.LEFT);
            } else {
                state.player.move(Direction.RIGHT);
            }
        } else {
            state.player.move(Direction.NONE);
        }
    }

    private boolean isKeyPressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        var state = GameState.INSTANCE;
        return switch (keycode) {
            case L: {
                state.debug.enabled = !state.debug.enabled;
                Gdx.app.log("DEBUG", state.debug.enabled ? "ImGui enabled" : "ImGui disabled");
                yield true;
            }
            case SPACE:
            case UP:
            case W: {
                state.player.setJump(true);
                yield state.player.move(Direction.UP);
            }
            case DOWN:
            case S: {
                state.player.setStamp(true);
                yield state.player.move(Direction.DOWN);
            }
            default: yield false;
        };
    }

    @Override
    public boolean keyUp(int keycode) {
        var state = GameState.INSTANCE;
        return switch (keycode) {
            case SPACE:
            case UP:
            case W: {
                state.player.setJump(false);
                yield true;
            }
            case DOWN:
            case S: {
                state.player.setStamp(false);
                yield true;
            }
            default: yield false;
        };
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastMouseButton = button;
        return worldEdit(screenX, screenY);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return worldEdit(screenX, screenY);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        var state = GameState.INSTANCE;
        if (state.debug.enabled && amountY != 0) {
            state.camera.zoom += amountY * 0.1f;
            return true;
        }
        return false;
    }

    private boolean worldEdit(int screenX, int screenY) {
        var state = GameState.INSTANCE;
        if (state.debug.enabled && state.debug.showWorldedit.get()) {
            Vec2i pos = DebugUtils.getTilePosFromScreenPos(new Vec2i(screenX, screenY));
            try {
                if (lastMouseButton == Input.Buttons.LEFT) {
                    state.world.removeAt(pos);
                    state.world.addAt(pos, state.debug.selectedWorldObject);
                    return true;
                } else if (lastMouseButton == Input.Buttons.RIGHT) {
                    state.world.removeAt(pos);
                    return true;
                } else if (lastMouseButton == Input.Buttons.MIDDLE) {
                    state.debug.selectedWorldObject = state.world.at(pos);
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
        return false;
    }
}
