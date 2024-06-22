package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.ChatMessage;

import java.util.List;

/**
 * This class represents a response message containing a list of chat messages.
 * It extends the GenericResponseMessage class.
 */
public class FetchChatResponseMessage extends GenericResponseMessage {
    private final List<ChatMessage> chat;

    /**
     * Constructs a new FetchChatResponseMessage with the specified list of chat messages.
     *
     * @param chat a list of chat messages.
     */
    public FetchChatResponseMessage(List<ChatMessage> chat) {
        this.chat = chat;
    }

    /**
     * Returns the list of chat messages.
     *
     * @return a list of chat messages.
     */
    public List<ChatMessage> getChat() {
        return chat;
    }
}
