package it.polimi.ingsw.network.messages.responses;

/**
 * Represents a response message that contains the ID of the starting card in a game.
 * This message is sent in response to a request for the starting card ID.
 */
public class GetStartingCardResponseMessage extends GenericResponseMessage{
    private final Integer startingCardId;

    /**
     * Constructs a new GetStartingCardResponseMessage with the specified starting card ID.
     * @param startingCardId the ID of the starting card in the game.
     */
    public GetStartingCardResponseMessage(Integer startingCardId) {
        this.startingCardId = startingCardId;
    }

    /**
     * Returns the ID of the starting card in the game.
     * @return the starting card ID.
     */
    public Integer getStartingCardId() {
        return startingCardId;
    }
}
