package it.polimi.ingsw.network.messages.responses;

import java.util.Deque;

/**
 * This class represents a response message containing the placing order of players.
 * It extends the GenericResponseMessage class.
 */
public class GetPlacingOrderResponseMessage extends GenericResponseMessage {
    private final Deque<Integer> placingOrder;
    private final String usernameRequiredData;

    /**
     * Constructs a new GetPlacingOrderResponseMessage with the specified placing order and username data.
     *
     * @param placingOrder         a deque representing the placing order of players.
     * @param usernameRequiredData the username of the player for whom the data is required.
     */
    public GetPlacingOrderResponseMessage(Deque<Integer> placingOrder, String usernameRequiredData) {
        this.placingOrder = placingOrder;
        this.usernameRequiredData = usernameRequiredData;
    }

    /**
     * Returns the deque representing the placing order of players.
     *
     * @return a deque representing the placing order of players.
     */
    public Deque<Integer> getPlacingOrder() {
        return placingOrder;
    }

    /**
     * Retrieves the username of the player for whom the data is required.
     *
     * @return the username of the player for whom the data is required.
     */
    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
