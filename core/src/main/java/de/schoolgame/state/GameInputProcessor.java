package de.schoolgame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.badlogic.gdx.Input.Keys.L;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import com.badlogic.gdx.InputProcessor;

import de.schoolgame.primitives.Direction;
import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.utils.CoordinateUtils;
import de.schoolgame.utils.Save;
import de.schoolgame.world.World;
import de.schoolgame.world.entities.PlayerEntity;

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

        return switch (state.state) {
            case MAIN_MENU:
                yield mainMenu(screenX, screenY);
            case WORLD_SELECT:
                yield worldSelect(screenX, screenY);
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
        if ((state.state == GameState.GameStateType.DEBUG && state.debug.showWorldedit.get()) || state.state == GameState.GameStateType.WORLD_EDITOR) {
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

    private boolean worldSelect(int screenX, int screenY) {
        var state = GameState.INSTANCE;
        var camera = state.camera;

        Vec2f pos = CoordinateUtils.getCameraPosFromScreenPos(new Vec2i(screenX, screenY)).toVec2f();

        final int buttonSpacing = 10;

        final Vec2i campaignButtonSize = new Vec2i(64, 64);
        final int campaignSpacing = campaignButtonSize.x + buttonSpacing;

        final int font_size = 3;
        final int font_height = font_size * 7;

        int x = buttonSpacing;
        int y = camera.viewSize.y;

        y -= font_height + buttonSpacing;

        y -= buttonSpacing + campaignButtonSize.y;
        for (int i = 0; i < 7; i++) {
            Rect rect = new Rect(new Vec2f(x, y), campaignButtonSize.toVec2f());

            if (rect.contains(pos)) {
                Gdx.app.log("WorldSelect", "Selected world: " + i);
                Save save = state.worldManager.get("world_" + i);
                state.loadSave(save);
                state.state = GameState.GameStateType.GAME;
                return true;
            }

            x += campaignSpacing;
        }

        return false;
    }

    private boolean mainMenu(int screenX, int screenY) {
        var state = GameState.INSTANCE;
        var camera = state.camera;

        Vec2f buttonSize = new Vec2i(300, 64).toVec2f();
        final int buttonSpacing = 10;

        Vec2f pos = CoordinateUtils.getCameraPosFromScreenPos(new Vec2i(screenX, screenY)).toVec2f();

        int travel = (int) (buttonSize.y + buttonSpacing);
        int x = (int) ((camera.viewSize.x - buttonSize.x) / 2);
        int y = buttonSpacing;

        Rect exit = new Rect(new Vec2f(x, y), buttonSize);
        y += travel;
        Rect create = new Rect(new Vec2f(x, y), buttonSize);
        y += travel;
        Rect start = new Rect(new Vec2f(x, y), buttonSize);

        if (exit.contains(pos)) {
            Gdx.app.exit();
        } else if (start.contains(pos)) {
            state.state = GameState.GameStateType.WORLD_SELECT;
        } else if (create.contains(pos)) {
            state.world = new World();
            state.player = new PlayerEntity(Vec2f.ONE);
            state.player.setGodmode(true);
            state.state = GameState.GameStateType.WORLD_EDITOR;
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
