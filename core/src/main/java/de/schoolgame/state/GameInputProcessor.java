package de.schoolgame.state;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.schoolgame.utils.DebugUtils;
import de.schoolgame.world.entities.Player;

public class GameInputProcessor implements InputProcessor {
    private int lastMouseButton = Input.Buttons.LEFT;

    @Override
    public boolean keyDown(int keycode) {
        var state = GameState.INSTANCE;

        return switch (keycode) {
            case Input.Keys.A, Input.Keys.LEFT -> {
                state.player.setPlayerState(Player.PlayerState.MOVING_LEFT);
                yield true;
            }
            case Input.Keys.D, Input.Keys.RIGHT -> {
                state.player.setPlayerState(Player.PlayerState.MOVING_RIGHT);
                yield true;
            }
            default -> false;
        };
    }

    @Override
    public boolean keyUp(int keycode) {
        var state = GameState.INSTANCE;

        return switch (keycode) {
            case Input.Keys.A, Input.Keys.LEFT -> {
                if (state.player.getPlayerState() == Player.PlayerState.MOVING_LEFT) {
                    state.player.setPlayerState(Player.PlayerState.IDLE);
                }
                yield true;
            }
            case Input.Keys.D, Input.Keys.RIGHT -> {
                if (state.player.getPlayerState() == Player.PlayerState.MOVING_RIGHT) {
                    state.player.setPlayerState(Player.PlayerState.IDLE);
                }
                yield true;
            }
            default -> false;
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
        return false;
    }

    private boolean worldEdit(int screenX, int screenY) {
        var state = GameState.INSTANCE;
        if (state.debug.enabled && state.debug.showWorldedit.get()) {
            int[] pos = DebugUtils.getTilePosFromScreenPos(screenX, screenY);
            if (lastMouseButton == Input.Buttons.LEFT) {
                state.world.removeAt(pos[0], pos[1]);
                state.world.addAt(pos[0], pos[1], state.debug.selectedTile);
                return true;
            } else if (lastMouseButton == Input.Buttons.RIGHT) {
                state.world.removeAt(pos[0], pos[1]);
                return true;
            } else if (lastMouseButton == Input.Buttons.MIDDLE) {
                state.debug.selectedTile = state.world.at(pos[0], pos[1]);
            }
        }
        return false;
    }
}
