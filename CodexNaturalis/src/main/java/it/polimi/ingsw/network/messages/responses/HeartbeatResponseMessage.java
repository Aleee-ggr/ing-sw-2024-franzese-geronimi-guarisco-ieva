package it.polimi.ingsw.network.messages.responses;

import java.util.UUID;

public class HeartbeatResponseMessage extends GenericResponseMessage{
    private final UUID gameUUID;

    public HeartbeatResponseMessage(UUID gameUUID) {
        this.gameUUID = gameUUID;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}
