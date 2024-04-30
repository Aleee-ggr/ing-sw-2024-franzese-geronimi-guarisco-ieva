package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientChooseStartingObjective extends GenericRequestMessage{
    private final UUID gameUUID;
    private final Integer objectiveID;

    public SocketClientChooseStartingObjective(String username, UUID gameUUID, Integer objectiveID) {
        this.username = username;
        this.gameUUID = gameUUID;
        this.objectiveID = objectiveID;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public Integer getObjectiveID() {
        return objectiveID;
    }
}
