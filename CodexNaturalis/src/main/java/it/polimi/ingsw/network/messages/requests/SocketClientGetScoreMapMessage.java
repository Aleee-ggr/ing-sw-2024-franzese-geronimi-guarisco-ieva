package it.polimi.ingsw.network.messages.requests;

public class SocketClientGetScoreMapMessage extends GenericRequestMessage {

    public SocketClientGetScoreMapMessage(String username){
        this.username = username;
    }
}
