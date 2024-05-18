package it.polimi.ingsw.network.messages.responses;

import java.util.Deque;

public class GetPlacingOrderResponseMessage extends GenericResponseMessage{
    private final Deque<Integer> placingOrder;
    private final String usernameRequiredData;

    public GetPlacingOrderResponseMessage(Deque<Integer> placingOrder, String usernameRequiredData) {
        this.placingOrder = placingOrder;
        this.usernameRequiredData = usernameRequiredData;
    }

    public Deque<Integer> getPlacingOrder() {
        return placingOrder;
    }

    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
