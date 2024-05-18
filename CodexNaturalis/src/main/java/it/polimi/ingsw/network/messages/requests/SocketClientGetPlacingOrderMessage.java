package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientGetPlacingOrderMessage extends GenericRequestMessage{
    private final UUID gameUUID;
    private final String usernameRequiredData;


    public SocketClientGetPlacingOrderMessage(String username, UUID gameUUID, String usernameRequiredData){
        this.username = username;
        this.gameUUID = gameUUID;
        this.usernameRequiredData = usernameRequiredData;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
