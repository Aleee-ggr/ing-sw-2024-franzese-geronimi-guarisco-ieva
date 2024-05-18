package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;
import java.util.UUID;

public class FetchAvailableGamesResponseMessage extends GenericResponseMessage{
    private final ArrayList<UUID> availableGames;

    public FetchAvailableGamesResponseMessage(ArrayList<UUID> availableGames) {
        this.availableGames = availableGames;
    }

    public ArrayList<UUID> getAvailableGames() {
        return availableGames;
    }
}
