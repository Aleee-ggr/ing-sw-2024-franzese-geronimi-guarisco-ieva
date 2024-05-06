package it.polimi.ingsw.network.messages.responses;

public class GetStartingCardResponseMessage extends GenericResponseMessage{
    private final Integer startingCardId;

    public GetStartingCardResponseMessage(Integer startingCardId) {
        this.startingCardId = startingCardId;
    }

    public Integer getStartingCardId() {
        return startingCardId;
    }
}
