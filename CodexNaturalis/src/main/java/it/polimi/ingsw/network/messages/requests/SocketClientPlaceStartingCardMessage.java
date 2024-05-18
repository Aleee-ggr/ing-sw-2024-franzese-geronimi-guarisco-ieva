package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientPlaceStartingCardMessage extends GenericRequestMessage{
    private final UUID gameUUID;
    private final boolean frontSideUp;

    public SocketClientPlaceStartingCardMessage(String username, UUID gameUUID, boolean frontSideUp) {
        this.username = username;
        this.gameUUID = gameUUID;
        this.frontSideUp = frontSideUp;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public boolean isFrontSideUp() {
        return frontSideUp;
    }
}
