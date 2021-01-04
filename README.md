# nl-chat

### Feature list
 - Aliases for user IPs stored in a file on the server (decided by users)
 - Chat groups with IDs (visible to the user)
 - Server handles everything. Clients send request to the server.
 - Both the server and the clients store files with data
 - Automatically removes a server when no one is in (notifying the last user)
 - Two threads: the main thread, and a thread that waits for messages

 classe astratta Message con un metodo OnReceive(byte[])
 una classe per ogni tipo di messaggio che deriva da Message
 MessageManager che contiene un array statico di Message

### Project structure
 - User: stores the IP, the name of a user and its chat list
 - ChatGroup: stores a list of users, manage messages
 - MessageType: int (1 byte) that stores a type of client request (user message, create group, join group, leave group, remove group, get group code, group code, get group, group)
#### Client
 - NLClient (main): receive messages from the server and handles them, send messages to the server
 - graphics...
#### Server
 - GroupManager: handles all the existing groups (creates groups, removes groups, stores groups)
 - NLServer (main): receive messages from the client and handles them, send messages to the client

### Packet structure:
 - 1 byte for the message type
 - content (string, int or both) optional

