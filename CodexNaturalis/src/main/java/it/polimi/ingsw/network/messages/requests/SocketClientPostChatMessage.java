package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message sent by a socket client to post a chat message in a game.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 *
 * @see GenericRequestMessage
 */
public class SocketClientPostChatMessage extends GenericRequestMessage {
    private final UUID gameUUID;
    private final String message;
    private final String receiver;

    /**
     * Constructs a SocketClientPostChatMessage object.
     *
     * @param username The username of the client sending the chat message.
     * @param gameUUID The UUID of the game in which the chat message is being posted.
     * @param message  The content of the chat message.
     * @param receiver The username of the message receiver, or null if it's a broadcast message.
     */
    public SocketClientPostChatMessage(String username, UUID gameUUID, String message, String receiver) {
        this.username = username;
        this.gameUUID = gameUUID;
        this.message = message;
        this.receiver = receiver;
    }

    /**
     * Retrieves the UUID of the game in which the chat message is being posted.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }

    /**
     * Retrieves the content of the chat message.
     *
     * @return The content of the chat message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieves the username of the message receiver, or null if it's a broadcast message.
     *
     * @return The username of the message receiver, or null if it's a broadcast message.
     */
    public String getReceiver() {
        return receiver;
    }
}
