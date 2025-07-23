package de.schoolgame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.render.Sound;
import de.schoolgame.utils.CoordinateUtils;

import static com.badlogic.gdx.Input.Keys.*;

public class GameInputProcessor implements InputProcessor {
    private int lastMouseButton = Input.Buttons.LEFT;

    public void update() {
        var state = GameState.INSTANCE;

        if (state.controllable()) {
            var leftPressed = isKeyPressed(A) || isKeyPressed(LEFT);
            var rightPressed = isKeyPressed(D) || isKeyPressed(RIGHT);
            if (leftPressed != rightPressed) {
                if (leftPressed) {
                    state.player.move(Direction.LEFT);
                } else {
                    state.player.move(Direction.RIGHT);
                }
            } else {
                state.player.cancelMovement(Direction.LEFT);
                state.player.cancelMovement(Direction.RIGHT);
            }

            var upPressed = isKeyPressed(W) || isKeyPressed(UP) || isKeyPressed(SPACE);
            var downPressed = isKeyPressed(S) || isKeyPressed(DOWN);
            if (upPressed != downPressed) {
                if (upPressed) {
                    state.player.cancelMovement(Direction.DOWN);
                    state.player.move(Direction.UP);
                } else {
                    state.player.cancelMovement(Direction.UP);
                    state.player.move(Direction.DOWN);
                }
            } else {
                state.player.cancelMovement(Direction.DOWN);
                state.player.cancelMovement(Direction.UP);
            }
        }
    }

    private boolean isKeyPressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        var state = GameState.INSTANCE;

        if (state.controllable()) {
            if (keycode == L) {
                if (state.getState() == GameState.GameStateType.DEBUG) {
                    state.setState(GameState.GameStateType.GAME);
                    Gdx.app.log("DEBUG", "ImGui disabled");
                    return true;
                } else if (state.getState() == GameState.GameStateType.GAME) {
                    state.setState(GameState.GameStateType.DEBUG);
                    Gdx.app.log("DEBUG", "ImGui enabled");
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        var state = GameState.INSTANCE;

        if (keycode == ESCAPE && state.getState() != GameState.GameStateType.CHARACTER_SELECT) {
            if (escape()) {
                state.setState(GameState.GameStateType.MAIN_MENU);
            }
            return true;
        }

        if (state.controllable()) {
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

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        var state = GameState.INSTANCE;
        if (state.getState() == GameState.GameStateType.CHARACTER_SELECT) {
            if (character == '\b' && !state.username.isEmpty()) {
                state.username = state.username.substring(0, state.username.length() - 1);
                return true;
            }

            byte c = (byte) character;
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                String name = state.username + character;
                if (name.length() >= 12) name = name.substring(0, 12);
                state.username = name;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastMouseButton = button;
        var state = GameState.INSTANCE;

        return switch (state.getState()) {
            case MAIN_MENU:
            case WORLD_SELECT:
            case SCOREBOARD:
            case CHARACTER_SELECT:
            case CREDITS:
                yield screen(screenX, screenY);
            case DEBUG:
                if (!state.debug.showWorldedit.get()) yield false;
            case WORLD_EDITOR:
                yield worldEdit(screenX, screenY);
            default: yield false;
        };
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
        if ((state.getState() == GameState.GameStateType.DEBUG && state.debug.showWorldedit.get()) || state.getState() == GameState.GameStateType.WORLD_EDITOR) {
            return worldEdit(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        var state = GameState.INSTANCE;
        if (state.getState() == GameState.GameStateType.DEBUG && amountY != 0) {
            state.camera.zoom += amountY * 0.1f;
            return true;
        }
        return false;
    }

    private boolean screen(int screenX, int screenY) {
        var state = GameState.INSTANCE;
        Vec2i pos = CoordinateUtils.getCameraPosFromScreenPos(new Vec2i(screenX, screenY));

        boolean result = state.screen.onClick(pos);

        if (result) {
            Sound sound = GameState.INSTANCE.assetManager.get("audio/brackeys/tap/tap", Sound.class);
            sound.play();
        }

        return result;
    }

    private boolean worldEdit(int screenX, int screenY) {
        var state = GameState.INSTANCE;
        Vec2i pos = CoordinateUtils.getTilePosFromScreenPos(new Vec2i(screenX, screenY));
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
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        return false;
    }

    public boolean escape() {
        var state = GameState.INSTANCE;

        if (state.escapeFlag) {
            state.escapeFlag = false;
            return true;
        }
        state.escapeFlag = true;

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Gdx.app.log("ERROR", "Escape thread interrupted", e);
            }
            state.escapeFlag = false;
        }).start();
        return false;
    }
}
