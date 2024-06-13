package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.enums.Color;

import java.util.ArrayList;

public class GetAvailableColorsResponseMessage extends GenericResponseMessage{
    private final ArrayList<Color> availableColors;

    public GetAvailableColorsResponseMessage(ArrayList<Color> availableColors) {
        this.availableColors = availableColors;
    }

    public ArrayList<Color> getAvailableColors() {
        return availableColors;
    }
}