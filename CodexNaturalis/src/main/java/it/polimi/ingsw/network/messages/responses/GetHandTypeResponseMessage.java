package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

/**
 * This class represents a response message containing the hand type information.
 * It extends the GenericResponseMessage class.
 */
public class GetHandTypeResponseMessage extends GenericResponseMessage {
    private final ArrayList<Boolean> isGold;
    private final String usernameRequiredData;

    /**
     * Constructs a new GetHandTypeResponseMessage with the specified hand type and username data.
     *
     * @param isGold a list indicating whether each card in the hand is gold.
     * @param usernameRequiredData the username of the player for whom the data is required.
     */
    public GetHandTypeResponseMessage(ArrayList<Boolean> isGold, String usernameRequiredData) {
        this.isGold = isGold;
        this.usernameRequiredData = usernameRequiredData;
    }

    /**
     * Returns the list indicating whether each card in the hand is gold.
     *
     * @return a list indicating whether each card in the hand is gold.
     */
    public ArrayList<Boolean> getIsGold() {
        return isGold;
    }

    /**
     * Retrieves the username of the required data.
     *
     * @return the required username data
     */
    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
