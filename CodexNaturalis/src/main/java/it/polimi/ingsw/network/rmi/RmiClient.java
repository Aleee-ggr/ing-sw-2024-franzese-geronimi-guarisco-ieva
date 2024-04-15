package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.network.Client;

import java.util.UUID;

public class RmiClient extends Client {

    public RmiClient(UUID gameId, String playerUsername, String serverAddress, int serverPort) {
        super(gameId, playerUsername, serverAddress, serverPort);
    }
}
