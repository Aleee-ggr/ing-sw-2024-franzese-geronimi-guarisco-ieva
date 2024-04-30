package it.polimi.ingsw.network.messages.requests;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.UUID;

public class SocketClientPlaceCardMessage extends GenericRequestMessage {
    private final Coordinates coordinates;
    private final int cardId;
    private final UUID gameUUID;

    public SocketClientPlaceCardMessage(String username, Coordinates coordinates, int cardId, UUID gameUUID) {
        this.username = username;
        this.coordinates = coordinates;
        this.cardId = cardId;
        this.gameUUID = gameUUID;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getCardId() {
        return cardId;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}