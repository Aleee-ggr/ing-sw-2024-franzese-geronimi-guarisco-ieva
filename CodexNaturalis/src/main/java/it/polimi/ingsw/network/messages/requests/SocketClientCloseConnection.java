package it.polimi.ingsw.network.messages.requests;

public class SocketClientCloseConnection extends GenericRequestMessage{
    public SocketClientCloseConnection(String username){
        this.username = username;
    }
}