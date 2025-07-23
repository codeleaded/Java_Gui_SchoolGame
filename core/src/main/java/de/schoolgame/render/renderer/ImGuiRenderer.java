package de.schoolgame.render.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;

import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.utils.Save;
import de.schoolgame.world.WorldObject;
import de.schoolgame.world.entities.MovingEntity;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class ImGuiRenderer implements IRenderer {
    private ImGuiImplGlfw imGuiGlfw;
    private ImGuiImplGl3 imGuiGl3;
    private InputProcessor tmpProcessor;

    public ImGuiRenderer() {
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGl3 = new ImGuiImplGl3();
        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.getFonts().addFontDefault();
        io.getFonts().build();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 300 es");
    }

    public void render() {
        var state = GameState.INSTANCE;
        var debug = GameState.INSTANCE.debug;

        start();
        var viewport = ImGui.getMainViewport();

        if (GameState.INSTANCE.getState() == GameState.GameStateType.DEBUG) {
            ImGui.setNextWindowPos(viewport.getPos(), ImGuiCond.Once);
            if (ImGui.begin("Debug", null, ImGuiWindowFlags.AlwaysAutoResize)) {
                ImGui.checkbox("Show Metrics", state.debug.showMetrics);

                ImGui.checkbox("Show Worldedit", state.debug.showWorldedit);
                ImGui.checkbox("Show Guide", state.debug.showGuide);
                ImGui.checkbox("Show Demo", state.debug.showDemo);
            }
            ImGui.end();

            if (state.debug.showMetrics.get()) {
                ImGui.setNextWindowPos(viewport.getPos(), ImGuiCond.Once);
                ImGui.showMetricsWindow();
            }

            if (state.debug.showGuide.get()) {
                ImGui.setNextWindowPos(viewport.getPos(), ImGuiCond.Once);
                if (ImGui.begin("Guide", null, ImGuiWindowFlags.AlwaysAutoResize)) {
                    ImGui.showUserGuide();
                }
                ImGui.end();
            }

            if (state.debug.showDemo.get()) {
                ImGui.showDemoWindow();
            }
        }

        if (state.debug.showWorldedit.get()) {
            ImGui.setNextWindowPos(new ImVec2(viewport.getPosX() + viewport.getWorkSizeX() - 293, viewport.getPosY()), ImGuiCond.Once);
            if (ImGui.begin("World Editor", null, ImGuiWindowFlags.AlwaysAutoResize)) {
                ImGui.text("TILES");

                var selected = state.debug.selectedWorldObject;

                int i = 0;
                for (WorldObject o : WorldObject.values()) {
                    if (i == 0) {
                        i++;
                        continue;
                    }
                    if (i % 2 == 0) {
                        ImGui.sameLine();
                    }

                    ImGui.pushID(i);

                    if (ImGui.selectable(o.toString().toLowerCase(), o == selected, 0, new ImVec2(100, 12))) {
                        state.debug.selectedWorldObject = o;
                    }

                    ImGui.popID();
                    i++;
                }

                ImGui.separatorText("KEYBINDS");
                ImGui.text("Left MB: insert Tile");
                ImGui.text("Middle MB: copy Tile");
                ImGui.text("Right MB: remove Tile");
                ImGui.text("Scroll: zoom in/out");
                if (ImGui.button("Swap Gravity")) {
                    MovingEntity.GRAVITY *= -1;
                }

                ImGui.separatorText("Settings");

                ImGui.inputInt2("Size", debug.inputWorldSize);
                ImGui.sameLine();
                if (ImGui.button("Set")) {
                    state.world.setSize(new Vec2i(debug.inputWorldSize).max(new Vec2i(1, 1)));
                }

                ImGui.inputInt2("Spawn", state.world.getSpawn().toArray(), ImGuiInputTextFlags.ReadOnly);
                ImGui.sameLine();
                if (ImGui.button("Set##2")) {
                    var pos = state.player.getPosition().round();
                    Gdx.app.log("ImGui", "Set Spawn to " + pos);
                    state.world.setSpawn(pos);
                }

                ImGui.separatorText("Save/Load");

                ImGui.inputText("World Name", debug.inputWorldName);

                if (state.server.isConnected()) {
                    if (ImGui.button("Upload")) {
                        state.worldManager.upload(debug.inputWorldName.get());
                    }
                    if (ImGui.button("Download")) {
                        state.worldManager.download(debug.inputWorldName.get());
                    }
                } else {
                    if (ImGui.button("Save")) {
                        state.worldManager.save(debug.inputWorldName.get());
                        state.worldManager.load(debug.inputWorldName.get());
                    }
                    ImGui.sameLine();
                    if (ImGui.button("Load")) {
                        Save s = state.worldManager.get(debug.inputWorldName.get());
                        if (state.getState() == GameState.GameStateType.DEBUG) {
                            state.world.summonEntities();
                        }
                        state.loadSave(s);
                    }
                }


            }
            ImGui.end();
        }

        if (state.debug.showPlayerInfo.get()) {
            ImGui.setNextWindowPos(new ImVec2(viewport.getPosX() + viewport.getWorkSizeX() - 293 - 407, viewport.getPosY()), ImGuiCond.Once);
            if (ImGui.begin("Player", null, ImGuiWindowFlags.AlwaysAutoResize)) {

                debug.inputCoins.set("" + state.player.getCoins());
                ImGui.inputText("Coins", debug.inputCoins, ImGuiInputTextFlags.ReadOnly);
                ImGui.sameLine();
                if (ImGui.button("+")) {
                    state.player.setCoins(state.player.getCoins() + 1);
                }

                debug.inputPower.set("" + state.player.getPower());
                ImGui.inputText("Power", debug.inputPower, ImGuiInputTextFlags.ReadOnly);
                if (state.player.getPower() != 2) {
                    ImGui.sameLine();
                    if (ImGui.button("+##2")) {
                        state.player.setPower(state.player.getPower() + 1);
                    }
                }
                if (state.player.getPower() != 0) {
                    ImGui.sameLine();
                    if (ImGui.button("-##2")) {
                        state.player.setPower(state.player.getPower() - 1);
                    }
                }

                debug.inputStyle.set("" + state.playerStyle);
                ImGui.inputText("Style", debug.inputStyle, ImGuiInputTextFlags.ReadOnly);
                if (state.playerStyle != 7) {
                    ImGui.sameLine();
                    if (ImGui.button("+##3")) {
                        state.playerStyle ++;
                    }
                }
                if (state.playerStyle != 1) {
                    ImGui.sameLine();
                    if (ImGui.button("-##3")) {
                        state.playerStyle--;
                    }
                }

                ImGui.inputFloat2("Pos", state.player.getPosition().toArray(), ImGuiInputTextFlags.ReadOnly);
                ImGui.checkbox("Dead", state.player.getDead());

                ImGui.checkbox("Godmode", debug.inputGodmode);
                state.player.setGodmode(debug.inputGodmode.get());

                ImGui.checkbox("onGround",state.player.getGround());
                ImGui.checkbox("onWall",state.player.getWall());
            }
            ImGui.end();
        }

        end();
    }

    private void start() {
        if (tmpProcessor != null) { // Restore the input processor after ImGui caught all inputs, see #end()
            Gdx.input.setInputProcessor(tmpProcessor);
            tmpProcessor = null;
            GameState.INSTANCE.SetControllable(true);
        }

        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    private void end() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        // If ImGui wants to capture the input, disable libGDX's input processor
        if (ImGui.getIO().getWantCaptureKeyboard() || ImGui.getIO().getWantCaptureMouse()) {
            tmpProcessor = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(null);
            GameState.INSTANCE.SetControllable(false);
        }
    }

    public void dispose() {
        imGuiGl3.shutdown();
        imGuiGl3 = null;
        imGuiGlfw.shutdown();
        imGuiGlfw = null;
        ImGui.destroyContext();
    }


}
