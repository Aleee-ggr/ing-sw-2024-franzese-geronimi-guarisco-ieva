package it.polimi.ingsw.network.messages.responses;

public class PlaceCardResponseMessage extends GenericResponseMessage{
    private final boolean isPlaced;

    public PlaceCardResponseMessage(boolean isPlaced) {
        this.isPlaced = isPlaced;
    }

    public boolean isPlaced() {
        return isPlaced;
    }
}