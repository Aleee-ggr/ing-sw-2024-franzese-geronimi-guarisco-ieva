package it.polimi.ingsw.network.messages.requests;

import java.io.Serializable;

/**
 * Abstract class for messages used in network communication via sockets.
 * Represents a generic request message.
 * All request messages exchanged in network communication via sockets should extend this class.
 * This class implements the `Serializable` interface to allow messages to be serialized
 * for transmission across the network.
 **/
public abstract class GenericRequestMessage implements Serializable {
    protected String username;

    /**
     * Retrieves the username of the sender of the message.
     *
     * @return A string representing the username of the sender.
     */
    public String getUsername() {
        return username;
    }

}
