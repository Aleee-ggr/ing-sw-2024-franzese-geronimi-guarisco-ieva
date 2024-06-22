package it.polimi.ingsw.network.messages.responses;

import java.util.UUID;

/**
 * This class represents a response message confirming a heartbeat with a game UUID.
 * It extends the GenericResponseMessage class.
 */
public class HeartbeatResponseMessage extends GenericResponseMessage{
    private final UUID gameUUID;

    /**
     * Constructs a new HeartbeatResponseMessage with the specified game UUID.
     *
     * @param gameUUID the UUID of the game associated with the heartbeat.
     */
    public HeartbeatResponseMessage(UUID gameUUID) {
        this.gameUUID = gameUUID;
    }

    /**
     * Returns the UUID of the game associated with the heartbeat.
     *
     * @return the UUID of the game associated with the heartbeat.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
