package it.polimi.ingsw.network.messages.responses;

public class PostChatResponseMessage extends GenericResponseMessage{
    private final String isPosted;
    private final String message;

    public PostChatResponseMessage(String isPosted, String message) {
        this.isPosted = isPosted;
        this.message = message;
    }

    public String getIsPosted() {
        return isPosted;
    }

    public String getMessage() {
        return message;
    }
}