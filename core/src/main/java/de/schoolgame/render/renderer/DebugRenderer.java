package de.schoolgame.render.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import de.schoolgame.primitives.Vec2i;
import de.schoolgame.state.GameState;
import de.schoolgame.world.WorldObject;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImString;

public class DebugRenderer implements IRenderer {
    private ImGuiImplGlfw imGuiGlfw;
    private ImGuiImplGl3 imGuiGl3;
    private InputProcessor tmpProcessor;

    private final int[] inputWorldSize;
    private final ImString inputCoins;
    private final ImString inputPower;

    public DebugRenderer() {
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

        inputWorldSize = new int[2];
        inputCoins = new ImString("" + Integer.MAX_VALUE);
        inputPower = new ImString("0");
    }

    public void render() {
        var state = GameState.INSTANCE;

        start();
        var viewport = ImGui.getMainViewport();

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

                if (ImGui.collapsingHeader("World")) {
                    ImGui.separatorText("Settings");

                    ImGui.inputInt2("Size", inputWorldSize);
                    ImGui.sameLine();
                    if (ImGui.button("Set")) {
                        state.world.setSize(new Vec2i(inputWorldSize));
                    }

                    ImGui.inputInt2("Spawn", state.world.getSpawn().toArray(), ImGuiInputTextFlags.ReadOnly);

                    if (ImGui.button("Set Spawn")) {
                        var pos = state.player.getPosition().round();
                        Gdx.app.log("ImGui", "Set Spawn to " + pos);
                        state.world.setSpawn(pos);
                    }
                    if (ImGui.button("Save World")) {
                        state.writeSave();
                    }
                }


            }
            ImGui.end();
        }

        if (state.debug.showPlayerInfo.get()) {
            ImGui.setNextWindowPos(new ImVec2(viewport.getPosX() + viewport.getWorkSizeX() - 293 - 407, viewport.getPosY()), ImGuiCond.Once);
            if (ImGui.begin("Player", null, ImGuiWindowFlags.AlwaysAutoResize)) {

                inputCoins.set("" + state.player.getCoins());
                ImGui.inputText("Coins", inputCoins, ImGuiInputTextFlags.ReadOnly);
                ImGui.sameLine();
                if (ImGui.button("+1")) {
                    state.player.setCoins(state.player.getCoins() + 1);
                }

                inputPower.set("" + state.player.getPower());
                ImGui.inputText("Power", inputPower, ImGuiInputTextFlags.ReadOnly);
                if (state.player.getPower() != 2) {
                    ImGui.sameLine();
                    if (ImGui.button("PowerUp")) {
                        state.player.setPower(state.player.getPower() + 1);
                    }
                }
                if (state.player.getPower() != 0) {
                    ImGui.sameLine();
                    if (ImGui.button("PowerDown")) {
                        state.player.setPower(state.player.getPower() - 1);
                    }
                }

                ImGui.inputFloat2("Pos", state.player.getPosition().toArray(), ImGuiInputTextFlags.ReadOnly);
                ImGui.checkbox("Dead", state.player.getDead());
            }
            ImGui.end();
        }

        end();
    }

    private void start() {
        if (tmpProcessor != null) { // Restore the input processor after ImGui caught all inputs, see #end()
            Gdx.input.setInputProcessor(tmpProcessor);
            tmpProcessor = null;
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
