package de.schoolgame.state;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.schoolgame.utils.DebugUtils;
import de.schoolgame.world.entities.Player;

public class GameInputProcessor implements InputProcessor {

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
        var state = GameState.INSTANCE;

        if (button == Input.Buttons.LEFT && state.debug.enabled && state.debug.showWorldedit.get()) {
            int[] pos = DebugUtils.getTilePosFromScreenPos(screenX, screenY);
            GameState.INSTANCE.world.setAt(pos[0], pos[1], state.debug.selectedTile);
            return true;
        }
        return false;
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
        var state = GameState.INSTANCE;

        if (state.debug.enabled && state.debug.showWorldedit.get()) {
            int[] pos = DebugUtils.getTilePosFromScreenPos(screenX, screenY);
            GameState.INSTANCE.world.setAt(pos[0], pos[1], state.debug.selectedTile);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
