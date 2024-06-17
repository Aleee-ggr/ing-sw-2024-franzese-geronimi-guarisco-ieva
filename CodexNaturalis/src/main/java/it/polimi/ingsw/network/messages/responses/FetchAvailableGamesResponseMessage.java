package it.polimi.ingsw.network.messages.responses;

import java.util.Map;
import java.util.UUID;

public class FetchAvailableGamesResponseMessage extends GenericResponseMessage {
    private final Map<UUID, String> availableGames;

    public FetchAvailableGamesResponseMessage(Map<UUID, String> availableGames) {
        this.availableGames = availableGames;
    }

    public Map<UUID, String> getAvailableGames() {
        return availableGames;
    }
}
