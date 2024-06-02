package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

public class FetchChatResponseMessage extends GenericResponseMessage {
    private final ArrayList<String> chat;

    public FetchChatResponseMessage(ArrayList<String> chat) {
        this.chat = chat;
    }

    public ArrayList<String> getChat() {
        return chat;
    }
}
