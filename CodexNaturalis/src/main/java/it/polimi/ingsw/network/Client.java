package it.polimi.ingsw.network;

import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.model.cards.Card;

import java.util.UUID;

public abstract class Client {
    protected UUID gameId;
    protected final String playerUsername;
    protected final String serverAddress;
    protected final int serverPort;

    public Client(String playerUsername, String serverAddress, int serverPort) {
        this.playerUsername = playerUsername;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
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
