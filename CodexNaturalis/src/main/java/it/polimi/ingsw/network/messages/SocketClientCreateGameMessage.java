package it.polimi.ingsw.network.messages;

/**
 * Class representing a message from a client to create a new game in the network communication via sockets.
 * This class extends the `Message` class, inheriting the username field.
 * It is used when a client wants to create a new game.
 *
 * @author gloriageronimi
 */
public class SocketClientCreateGameMessage extends Message{

    /**
     * Constructor for the SocketClientCreateGameMessage class.
     *
     * @param username The username of the client sending the message.
     */
    public SocketClientCreateGameMessage(String username) {
        this.username = username;
    }
}
