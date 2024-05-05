package it.polimi.ingsw.network.messages.responses;

import java.util.UUID;

public class CreateGameResponseMessage extends GenericResponseMessage {
    private final UUID gameUUID;

    public CreateGameResponseMessage(UUID gameUUID) {
        this.gameUUID = gameUUID;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}
