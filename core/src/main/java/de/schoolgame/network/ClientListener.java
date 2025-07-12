package de.schoolgame.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener implements Listener {
    @Override
    public void received(Connection connection, Object object) {
        System.out.println(object);
        if (object instanceof Packet p) {
            p.handle(connection);
        }
    }


}
