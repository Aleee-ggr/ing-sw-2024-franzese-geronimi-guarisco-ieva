package it.polimi.ingsw.network.messages.requests;

/**
 * Represents a message from a client to request information about the score map in a game
 * via socket network communication.
 * This class extends the GenericRequestMessage class and contains the username of the client making the request.
 */
public class SocketClientGetScoreMapMessage extends GenericRequestMessage {

    /**
     * Constructs a new SocketClientGetScoreMapMessage with the specified username.
     * @param username The username of the client making the request.
     */
    public SocketClientGetScoreMapMessage(String username){
        this.username = username;
    }
}
