package it.polimi.ingsw.network.messages.responses;

/**
 * This class represents a response message indicating whether a starting card was successfully placed.
 * It extends the GenericResponseMessage class.
 */
public class PlaceStartingCardResponseMessage extends GenericResponseMessage {
    private final boolean isPlaced;

    /**
     * Constructs a new PlaceStartingCardResponseMessage with the specified placement status.
     *
     * @param isPlaced a boolean indicating whether the starting card was successfully placed.
     */
    public PlaceStartingCardResponseMessage(boolean isPlaced) {
        this.isPlaced = isPlaced;
    }

    /**
     * Returns whether the starting card was successfully placed.
     *
     * @return true if the starting card was successfully placed; false otherwise.
     */
    public boolean isPlaced() {
        return isPlaced;
    }
}
