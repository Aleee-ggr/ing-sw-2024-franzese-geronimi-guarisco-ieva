package it.polimi.ingsw.network.messages.responses;

import java.util.UUID;

/**
 * The JoinGameResponseMessage class represents a response to a request to join a game.
 * It contains information about the outcome of the request, including the UUID of the game
 * and whether the join was successful.
 */
public class JoinGameResponseMessage extends GenericResponseMessage {
    private final boolean joinedGame;
    private final UUID gameUUID;

    /**
     * Constructs a new JoinGameResponseMessage instance with the specified properties.
     *
     * @param gameUUID   The UUID of the game that the player is attempting to join.
     * @param joinedGame Indicates whether the join attempt was successful.
     */
    public JoinGameResponseMessage(UUID gameUUID, boolean joinedGame) {
        this.gameUUID = gameUUID;
        this.joinedGame = joinedGame;
    }

    /**
     * Returns the UUID of the game that the player attempted to join.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }

    /**
     * Returns a boolean indicating whether the player successfully joined the game.
     *
     * @return true if the player successfully joined the game, otherwise false.
     */
    public boolean isJoinedGame() {
        return joinedGame;
    }
}
