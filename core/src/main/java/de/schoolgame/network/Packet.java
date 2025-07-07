package de.schoolgame.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class Packet {
    public abstract void write(ObjectOutputStream out) throws IOException;
    public abstract void read(ObjectInputStream in) throws IOException;
}
