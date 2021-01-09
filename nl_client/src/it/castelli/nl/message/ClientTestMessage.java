package it.castelli.nl.message;

import nl.messages.IMessage;

public class ClientTestMessage implements IMessage {
    @Override
    public void OnReceive(byte[] data) {
        System.out.println("è stato ricevuto un messaggio di test Client");
    }
}
