package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

/**
 * Represents a response message containing the visible cards' information.
 * Extends GenericResponseMessage.
 */
public class GetVisibleCardsResponseMessage extends GenericResponseMessage{
    private final ArrayList<Integer> visibleCards;

    /**
     * Constructs a GetVisibleCardsResponseMessage with the specified visible cards.
     *
     * @param visibleCards the list of visible cards
     */
    public GetVisibleCardsResponseMessage(ArrayList<Integer> visibleCards) {
        this.visibleCards = visibleCards;
    }

    /**
     * Retrieves the list of visible cards.
     *
     * @return the list of visible cards
     */
    public ArrayList<Integer> getVisibleCards() {
        return visibleCards;
    }
}
