package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.enums.Color;

public class GetPlayerColorResponseMessage extends GenericResponseMessage{
    private final Color playerColor;
    private final String usernameRequiredData;

    public GetPlayerColorResponseMessage(Color playerColor, String usernameRequiredData) {
        this.playerColor = playerColor;
        this.usernameRequiredData = usernameRequiredData;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
