package it.castelli.nl.messages;

public interface IMessage
{
    void OnReceive(byte[] data);
}
