package it.polimi.ingsw.network.messages.responses;

import java.util.Map;
import java.util.UUID;

/**
 * This class represents a response message containing a list of available games.
 * It extends the GenericResponseMessage class.
 */
public class FetchAvailableGamesResponseMessage extends GenericResponseMessage {
    private final Map<UUID, String> availableGames;

    /**
     * Constructs a new FetchAvailableGamesResponseMessage with the specified available games.
     *
     * @param availableGames a map of available games, where the key is the game ID (UUID) and the value is the game name.
     */
    public FetchAvailableGamesResponseMessage(Map<UUID, String> availableGames) {
        this.availableGames = availableGames;
    }

    /**
     * Returns the map of available games.
     *
     * @return a map of available games, where the key is the game ID (UUID) and the value is the game name.
     */
    public Map<UUID, String> getAvailableGames() {
        return availableGames;
    }
}
