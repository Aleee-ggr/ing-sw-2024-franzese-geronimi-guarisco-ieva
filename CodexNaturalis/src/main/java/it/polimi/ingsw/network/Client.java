package it.polimi.ingsw.network;

import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.player.Player;


import java.rmi.ServerError;
import java.util.UUID;

public abstract class Client {
    private final UUID gameId;
    private final String playerUsername;
    private final String serverAddress;
    private final int serverPort;

    public Client(UUID gameId, String playerUsername, String serverAddress, int serverPort) {
        this.gameId = gameId;
        this.playerUsername = playerUsername;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }
    public Integer getScore() throws ServerConnectionException{
        return null;
    }
    public String getPlayerBoard() throws ServerConnectionException{
        return null;
    }
    public String getSharedBoard() throws ServerConnectionException{
        return null;
    }
    public Card drawCard() throws ServerConnectionException{
        return null;
    }
    public void placeCard() throws ServerConnectionException{
    }
    public void joinGame() throws ServerConnectionException{
    }
}
