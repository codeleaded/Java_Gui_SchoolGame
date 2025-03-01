package de.schoolgame.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import de.schoolgame.state.GameState;
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
        start();
        var viewport = ImGui.getMainViewport();

        ImGui.setNextWindowPos(new ImVec2(viewport.getPosX(), viewport.getCenterY()), ImGuiCond.Once);
        ImGui.showMetricsWindow();

        ImGui.setNextWindowPos(viewport.getPos(), ImGuiCond.Once);
        if (ImGui.begin("Guide", null, ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.showUserGuide();
        }
        ImGui.end();

        ImGui.setNextWindowPos(viewport.getCenter(), ImGuiCond.Once);
        if (ImGui.begin("Debug", null, ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.colorPicker3("Background Color", GameState.INSTANCE.bg_color);
            ImGui.separator();
            ImGui.sliderFloat("coin animation delay", GameState.INSTANCE.animation_delay, 0.001f, 0.1f);
        }
        ImGui.end();

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
