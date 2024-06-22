package it.polimi.ingsw.network.messages.responses;

/**
 * This class represents a response message indicating whether a card was successfully placed.
 * It extends the GenericResponseMessage class.
 */
public class PlaceCardResponseMessage extends GenericResponseMessage{
    private final boolean isPlaced;

    /**
     * Constructs a new PlaceCardResponseMessage with the specified placement status.
     *
     * @param isPlaced a boolean indicating whether the card was successfully placed.
     */
    public PlaceCardResponseMessage(boolean isPlaced) {
        this.isPlaced = isPlaced;
    }

    /**
     * Returns whether the card was successfully placed.
     *
     * @return true if the card was successfully placed; false otherwise.
     */
    public boolean isPlaced() {
        return isPlaced;
    }
}