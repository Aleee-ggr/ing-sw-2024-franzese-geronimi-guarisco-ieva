package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

public class GetPlayersResponseMessage extends GenericResponseMessage{
    private final ArrayList<String> players;

    public GetPlayersResponseMessage(ArrayList<String> players) {
        this.players = players;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }
}
