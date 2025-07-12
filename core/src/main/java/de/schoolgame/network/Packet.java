package de.schoolgame.network;

import com.esotericsoftware.kryonet.Connection;

public interface Packet {
    void handle(Connection connection);
}
