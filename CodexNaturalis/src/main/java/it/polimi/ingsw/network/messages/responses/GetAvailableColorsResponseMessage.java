package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.enums.Color;

import java.util.ArrayList;

/**
 * This class represents a response message containing a list of available colors.
 * It extends the GenericResponseMessage class.
 */
public class GetAvailableColorsResponseMessage extends GenericResponseMessage {
    private final ArrayList<Color> availableColors;

    /**
     * Constructs a new GetAvailableColorsResponseMessage with the specified list of available colors.
     *
     * @param availableColors a list of available colors.
     */
    public GetAvailableColorsResponseMessage(ArrayList<Color> availableColors) {
        this.availableColors = availableColors;
    }

    /**
     * Returns the list of available colors.
     *
     * @return a list of available colors.
     */
    public ArrayList<Color> getAvailableColors() {
        return availableColors;
    }
}