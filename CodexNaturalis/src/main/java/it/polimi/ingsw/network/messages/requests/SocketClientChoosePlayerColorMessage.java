package it.polimi.ingsw.network.messages.requests;

import it.polimi.ingsw.model.enums.Color;

import java.util.UUID;

public class SocketClientChoosePlayerColorMessage extends GenericRequestMessage{
    private final UUID gameUUID;
    private final Color playerColor;

    public SocketClientChoosePlayerColorMessage(String username, Color playerColor, UUID gameUUID){
        this.username = username;
        this.playerColor = playerColor;
        this.gameUUID = gameUUID;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public Color getPlayerColor() {
        return playerColor;
    }
}
