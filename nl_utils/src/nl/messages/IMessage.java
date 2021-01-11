package nl.messages;

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
	void OnReceive(byte[] data);
}
