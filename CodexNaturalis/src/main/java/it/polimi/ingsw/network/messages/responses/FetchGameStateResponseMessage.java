package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.controller.threads.GameState;

/**
 * This class represents a response message containing the current game state.
 * It extends the GenericResponseMessage class.
 */
public class FetchGameStateResponseMessage extends GenericResponseMessage {
    private final GameState gameState;

    /**
     * Constructs a new FetchGameStateResponseMessage with the specified game state.
     *
     * @param gameState the current game state.
     */
    public FetchGameStateResponseMessage(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Returns the current game state.
     *
     * @return the current game state.
     */
    public GameState getGameState() {
        return gameState;
    }
}