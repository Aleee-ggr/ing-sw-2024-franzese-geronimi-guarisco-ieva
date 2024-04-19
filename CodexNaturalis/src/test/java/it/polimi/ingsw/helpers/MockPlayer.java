package it.polimi.ingsw.helpers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

public class MockPlayer extends Player {
    /**
     * Calling the Player
     *
     * @param username    it is the unique identifier of the player.
     * @param currentGame pointer to the instance of game the player is playing.
     */

    public MockPlayer(String username, Game currentGame) {
        super(username, currentGame);

    }

    public void setResource(Resource res, Integer amount) {
        playerResources.put(res, amount);
    }
}
