package it.castelli.nl.server.messages;

import it.castelli.nl.server.Connection;

/**
 * Interface for every class that will process an incoming message
 */
public interface IMessage
{
    /**
     * Called when a packet is received
     *
     * @param data The content of the packet received
     */
    void OnReceive(byte[] data, Connection connection);
}