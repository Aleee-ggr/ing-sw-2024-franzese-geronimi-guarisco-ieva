package it.polimi.ingsw.network.messages.responses;

import java.util.UUID;

public class JoinGameResponseMessage extends GenericResponseMessage {
    private final boolean joinedGame;
    private final UUID gameUUID;

    public JoinGameResponseMessage(UUID gameUUID, boolean joinedGame) {
        this.gameUUID = gameUUID;
        this.joinedGame = joinedGame;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public boolean isJoinedGame() {
        return joinedGame;
    }
}
