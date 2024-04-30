package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

public class GetVisibleCardsResponseMessage extends GenericResponseMessage{
    private final ArrayList<Integer> visibleCards;

    public GetVisibleCardsResponseMessage(ArrayList<Integer> visibleCards) {
        this.visibleCards = visibleCards;
    }

    public ArrayList<Integer> getVisibleCards() {
        return visibleCards;
    }
}
