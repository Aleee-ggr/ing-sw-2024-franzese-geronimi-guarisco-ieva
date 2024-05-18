package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.controller.threads.GameState;

public class FetchGameStateResponseMessage extends GenericResponseMessage{
    private final GameState gameState;

    public FetchGameStateResponseMessage(GameState gameState){
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}