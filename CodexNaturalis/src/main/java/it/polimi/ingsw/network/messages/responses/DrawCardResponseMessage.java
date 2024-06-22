package it.polimi.ingsw.network.messages.responses;

/**
 * This class represents a response message indicating the result of drawing a card.
 * It extends the GenericResponseMessage class.
 */
public class DrawCardResponseMessage extends GenericResponseMessage{
    private final Integer cardID;

    /**
     * Constructs a new DrawCardResponseMessage with the specified card ID.
     *
     * @param cardID the ID of the drawn card.
     */
    public DrawCardResponseMessage(Integer cardID) {
        this.cardID = cardID;
    }

    /**
     * Returns the ID of the drawn card.
     *
     * @return the ID of the drawn card.
     */
    public Integer getCardID() {
        return cardID;
    }
}