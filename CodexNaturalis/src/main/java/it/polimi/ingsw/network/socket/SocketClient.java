package it.polimi.ingsw.network.socket;


import it.polimi.ingsw.network.Client;

import java.util.UUID;

public class SocketClient extends Client {
    public SocketClient(UUID gameId, String playerUsername, String serverAddress, int serverPort) {
        super(gameId, playerUsername, serverAddress, serverPort);
    }
}
