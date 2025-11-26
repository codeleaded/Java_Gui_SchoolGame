package de.schoolgame.state;

import de.schoolgame.world.WorldObject;
import imgui.type.ImBoolean;
import imgui.type.ImString;

public class DebugState {
    public ImBoolean showMetrics = new ImBoolean(false);
    public ImBoolean showGuide = new ImBoolean(false);
    public ImBoolean showWorldedit = new ImBoolean(true);
    public ImBoolean showPlayerInfo = new ImBoolean(true);
    public ImBoolean showDemo = new ImBoolean(false);

    public final int[] inputWorldSize = new int[2];
    public final ImString inputCoins = new ImString("" + Integer.MAX_VALUE);
    public final ImString inputPower = new ImString("0");
    public final ImString inputStyle = new ImString("1");
    public final ImBoolean inputGodmode = new ImBoolean(false);
    public final ImString inputWorldName = new ImString(25);

    public WorldObject selectedWorldObject = WorldObject.GRASS;

}
