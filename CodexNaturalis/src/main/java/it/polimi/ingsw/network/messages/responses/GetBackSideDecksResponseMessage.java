package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

public class GetBackSideDecksResponseMessage extends GenericResponseMessage{
    private final ArrayList<Integer> backSideDecks;

    public GetBackSideDecksResponseMessage(ArrayList<Integer> backSideDecks) {
        this.backSideDecks = backSideDecks;
    }

    public ArrayList<Integer> getBackSideDecks() {
        return backSideDecks;
    }
}
