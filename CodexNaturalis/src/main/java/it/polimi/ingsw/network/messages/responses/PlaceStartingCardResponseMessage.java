package it.polimi.ingsw.network.messages.responses;

public class PlaceStartingCardResponseMessage extends GenericResponseMessage{
    private final boolean isPlaced;

    public PlaceStartingCardResponseMessage(boolean isPlaced) {
        this.isPlaced = isPlaced;
    }

    public boolean isPlaced() {
        return isPlaced;
    }
}
