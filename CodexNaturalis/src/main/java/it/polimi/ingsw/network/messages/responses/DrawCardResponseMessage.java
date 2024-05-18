package it.polimi.ingsw.network.messages.responses;

public class DrawCardResponseMessage extends GenericResponseMessage{
    private final Integer cardID;

    public DrawCardResponseMessage(Integer cardID) {
        this.cardID = cardID;
    }

    public Integer getCardID() {
        return cardID;
    }
}