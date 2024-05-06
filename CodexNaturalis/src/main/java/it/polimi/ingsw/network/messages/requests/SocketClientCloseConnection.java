package it.polimi.ingsw.network.messages.requests;

/**
 * Represents a request message sent by a client to close the connection.
 */
public class SocketClientCloseConnection extends GenericRequestMessage{

    /**
     * Constructs a new SocketClientCloseConnection request message.
     * @param username the username of the client sending the request to close the connection.
     */
    public SocketClientCloseConnection(String username){
        this.username = username;
    }
}