package de.schoolgame.state;

import de.schoolgame.world.WorldObject;
import imgui.type.ImBoolean;

public class DebugState {
    public boolean enabled = false;

    public ImBoolean showMetrics = new ImBoolean(false);
    public ImBoolean showGuide = new ImBoolean(false);
    public ImBoolean showWorldedit = new ImBoolean(true);
    public ImBoolean showDemo = new ImBoolean(false);

    public WorldObject selectedWorldObject = WorldObject.GRASS;

}
