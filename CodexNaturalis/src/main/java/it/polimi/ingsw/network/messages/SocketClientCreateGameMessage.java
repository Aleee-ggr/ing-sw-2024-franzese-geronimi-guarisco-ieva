package it.polimi.ingsw.network.messages;

public class SocketClientCreateGameMessage extends Message{
    public SocketClientCreateGameMessage(String username) {
        this.username = username;
    }
}
