package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

/**
 * Represents a response message containing the back side information of the decks.
 * Extends GenericResponseMessage.
 */
public class GetBackSideDecksResponseMessage extends GenericResponseMessage{
    private final ArrayList<Integer> backSideDecks;

    /**
     * Constructs a GetBackSideDecksResponseMessage with the specified back side decks.
     *
     * @param backSideDecks the list of back side decks
     */
    public GetBackSideDecksResponseMessage(ArrayList<Integer> backSideDecks) {
        this.backSideDecks = backSideDecks;
    }

    /**
     * Retrieves the list of back side decks.
     *
     * @return the list of back side decks
     */
    public ArrayList<Integer> getBackSideDecks() {
        return backSideDecks;
    }
}
