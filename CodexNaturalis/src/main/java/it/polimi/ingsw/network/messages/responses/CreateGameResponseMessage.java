package it.polimi.ingsw.network.messages.responses;

import java.util.UUID;

/**
 * The CreateGameResponseMessage class represents a response to a request to create a new game.
 * It contains the UUID of the created game.
 */
public class CreateGameResponseMessage extends GenericResponseMessage {
    private final UUID gameUUID;

    /**
     * Constructs a new CreateGameResponseMessage instance with the specified UUID of the created game.
     *
     * @param gameUUID The UUID of the newly created game.
     */
    public CreateGameResponseMessage(UUID gameUUID) {
        this.gameUUID = gameUUID;
    }

    /**
     * Returns the UUID of the created game.
     *
     * @return The UUID of the created game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
