package it.polimi.ingsw.helpers;

import it.polimi.ingsw.model.Game;

import java.util.UUID;

public abstract class GameFactory {
    public static Game fullGame() {
        return new Game(UUID.randomUUID());
    }
}
