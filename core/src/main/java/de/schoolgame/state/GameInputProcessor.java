package de.schoolgame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.utils.CoordinateUtils;
import de.schoolgame.utils.Save;
import de.schoolgame.world.World;
import de.schoolgame.world.entities.PlayerEntity;

import static com.badlogic.gdx.Input.Keys.*;
import static de.schoolgame.render.renderer.GuiRenderer.*;

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
                state.player.move(Direction.NONE);
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
            return switch (keycode) {
                case L: {
                    if (state.state == GameState.GameStateType.DEBUG) {
                        state.state = GameState.GameStateType.GAME;
                        Gdx.app.log("DEBUG", "ImGui disabled");
                        yield true;
                    } else if (state.state == GameState.GameStateType.GAME) {
                        state.state = GameState.GameStateType.DEBUG;
                        Gdx.app.log("DEBUG", "ImGui enabled");
                        yield true;
                    }
                    yield false;
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

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        var state = GameState.INSTANCE;

        if (keycode == ESCAPE) {
            if (escape()) {
                GameState.INSTANCE.state = GameState.GameStateType.MAIN_MENU;
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
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastMouseButton = button;
        var state = GameState.INSTANCE;
        if (state.state == GameState.GameStateType.DEBUG && state.debug.showWorldedit.get()) {
            return worldEdit(screenX, screenY);
        }
        if (state.state == GameState.GameStateType.MAIN_MENU) {
            return mainMenu(screenX, screenY);
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
        if (state.state == GameState.GameStateType.DEBUG && state.debug.showWorldedit.get()) {
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
        if (state.state == GameState.GameStateType.DEBUG && amountY != 0) {
            state.camera.zoom += amountY * 0.1f;
            return true;
        }
        return false;
    }

    private boolean mainMenu(int screenX, int screenY) {
        var state = GameState.INSTANCE;
        var camera = state.camera;

        Vec2f pos = CoordinateUtils.getCameraPosFromScreenPos(new Vec2i(screenX, screenY)).toVec2f();

        int travel = BUTTON_HEIGHT + BUTTON_SPACING;
        int x = (camera.viewSize.x - BUTTON_WIDTH) / 2;
        int y = BUTTON_SPACING;

        Rect exit = new Rect(new Vec2f(x, y), new Vec2f(BUTTON_WIDTH, BUTTON_HEIGHT));
        y += travel;
        Rect create = new Rect(new Vec2f(x, y), new Vec2f(BUTTON_WIDTH, BUTTON_HEIGHT));
        y += travel;
        Rect start = new Rect(new Vec2f(x, y), new Vec2f(BUTTON_WIDTH, BUTTON_HEIGHT));

        if (exit.contains(pos)) {
            Gdx.app.exit();
        } else if (start.contains(pos)) {
            Save s = state.worldManager.get("worlds/save");
            state.loadSave(s);
            state.state = GameState.GameStateType.GAME;
        } else if (create.contains(pos)) {
            state.world = new World();
            state.player = new PlayerEntity(Vec2f.ZERO);
            state.state = GameState.GameStateType.DEBUG;
        } else {
            return false;
        }

        return true;
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
