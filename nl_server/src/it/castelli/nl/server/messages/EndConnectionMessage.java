package it.castelli.nl.server.messages;

import it.castelli.nl.server.Connection;

public class EndConnectionMessage extends Message{

    @Override
    public void onReceive(byte[] data, Connection connection) {
        super.onReceive(data, connection);
        connection.interrupt();
    }
}
