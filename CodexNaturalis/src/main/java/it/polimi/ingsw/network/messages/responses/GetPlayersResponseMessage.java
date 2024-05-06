package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

/**
 * Represents a response message containing a list of players in a game.
 * This message is sent in response to a request for the list of players in a game.
 */
public class GetPlayersResponseMessage extends GenericResponseMessage{
    private final ArrayList<String> players;

    /**
     * Constructs a new GetPlayersResponseMessage with the specified list of players.
     * @param players the list of players in the game.
     */
    public GetPlayersResponseMessage(ArrayList<String> players) {
        this.players = players;
    }

    /**
     * Returns the list of players in the game.
     * @return the list of players.
     */
    public ArrayList<String> getPlayers() {
        return players;
    }
}
