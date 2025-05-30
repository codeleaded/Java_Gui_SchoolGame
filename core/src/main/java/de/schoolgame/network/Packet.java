package de.schoolgame.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {
    public abstract void write(DataOutputStream out) throws IOException;
    public abstract void read(DataInputStream in) throws IOException;
}
