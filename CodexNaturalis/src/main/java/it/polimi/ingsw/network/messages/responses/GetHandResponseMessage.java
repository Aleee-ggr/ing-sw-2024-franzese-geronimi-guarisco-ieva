package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

/**
 * Represents a response message containing the hand's information.
 * Extends GenericResponseMessage.
 */
public class GetHandResponseMessage extends GenericResponseMessage {
    private final ArrayList<Integer> handIds;

    /**
     * Constructs a GetHandResponseMessage with the specified hand IDs.
     *
     * @param handIds the list of hand IDs
     */
    public GetHandResponseMessage(ArrayList<Integer> handIds) {
        this.handIds = handIds;
    }

    /**
     * Retrieves the list of hand IDs.
     *
     * @return the list of hand IDs
     */
    public ArrayList<Integer> getHandIds() {
        return handIds;
    }

}
