package de.schoolgame.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import de.schoolgame.state.GameState;
import de.schoolgame.world.Tile;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class ImGuiRenderer implements IRenderer {
    private static ImGuiImplGlfw imGuiGlfw;
    private static ImGuiImplGl3 imGuiGl3;
    private static InputProcessor tmpProcessor;

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
            ImGui.setNextWindowPos(new ImVec2(viewport.getPosX() + viewport.getWorkSizeX() - 230, viewport.getPosY()), ImGuiCond.Once);
            if (ImGui.begin("World Editor", null, ImGuiWindowFlags.AlwaysAutoResize)) {
                ImGui.text("TILES");

                var selected = state.debug.selectedTile;

                int i = 0;
                for (Tile t : Tile.values()) {
                    if (i == 0) {
                        i++;
                        continue;
                    }
                    if (i % 2 == 0) {
                        ImGui.sameLine();
                    }

                    ImGui.pushID(i);

                    if (ImGui.selectable(t.toString().toLowerCase(), t == selected, 0, new ImVec2(100, 12))) {
                        state.debug.selectedTile = t;
                    }

                    ImGui.popID();
                    i++;
                }

                ImGui.separator();

                ImGui.text("KEYBINDS");
                ImGui.text("Left MB: insert Tile");
                ImGui.text("Middle MB: copy Tile");
                ImGui.text("Right MB: remove Tile");

                ImGui.separator();

                if (ImGui.button("Save World")) {
                    state.writeSave();
                }
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
