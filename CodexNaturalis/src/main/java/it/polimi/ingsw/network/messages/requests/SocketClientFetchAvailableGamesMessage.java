package it.polimi.ingsw.network.messages.requests;

/**
 * Represents a message from a socket client requesting available games from the server.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 */
public class SocketClientFetchAvailableGamesMessage extends GenericRequestMessage {

    /**
     * Constructs a SocketClientFetchAvailableGamesMessage object.
     *
     * @param username The username of the client sending the message.
     */
    public SocketClientFetchAvailableGamesMessage(String username) {
        this.username = username;
    }

}