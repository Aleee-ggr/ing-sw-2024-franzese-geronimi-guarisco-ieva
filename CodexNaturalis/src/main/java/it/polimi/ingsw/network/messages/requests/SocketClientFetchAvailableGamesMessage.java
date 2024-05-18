package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientFetchAvailableGamesMessage extends GenericRequestMessage{

    public SocketClientFetchAvailableGamesMessage(String username){
        this.username = username;
    }

}