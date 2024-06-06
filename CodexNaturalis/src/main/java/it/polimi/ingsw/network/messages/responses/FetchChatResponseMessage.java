package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.ChatMessage;

import java.util.List;

public class FetchChatResponseMessage extends GenericResponseMessage {
    private final List<ChatMessage> chat;

    public FetchChatResponseMessage(List<ChatMessage> chat) {
        this.chat = chat;
    }

    public List<ChatMessage> getChat() {
        return chat;
    }
}
