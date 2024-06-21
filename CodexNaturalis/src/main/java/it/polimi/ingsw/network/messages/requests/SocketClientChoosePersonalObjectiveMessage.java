package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a request message sent by a client to choose a starting objective in a game.
 * This class contains the necessary information, such as the username of the client, the game UUID,
 * and the ID of the chosen objective.
 *
 * @see GenericRequestMessage
 */
public class SocketClientChoosePersonalObjectiveMessage extends GenericRequestMessage{
    private final UUID gameUUID;
    private final Integer objectiveID;

    /**
     * Constructs a new SocketClientChoosePersonalObjectiveMessage message
     * with the specified username, game UUID, and objective ID.
     * @param username The username of the client making the request.
     * @param gameUUID The UUID of the game in which the objective is chosen.
     * @param objectiveID The ID of the chosen starting objective.
     */
    public SocketClientChoosePersonalObjectiveMessage(String username, Integer objectiveID, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
        this.objectiveID = objectiveID;
    }

    /**
     * Returns the UUID of the game in which the starting objective is chosen.
     * @return The game UUID.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }

    /**
     * Returns the ID of the chosen starting objective.
     * @return The objective ID.
     */
    public Integer getObjectiveID() {
        return objectiveID;
    }
}
