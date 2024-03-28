package it.polimi.ingsw.helpers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.Collections;
import java.util.Map;

public class MockPlayer extends Player {
    /**
     * Calling the Player
     *
     * @param username    it is the unique identifier of the player.
     * @param currentGame pointer to the instance of game the player is playing.
     */

    Map<Resource, Integer> mockResources;
    public MockPlayer(String username, Game currentGame) {
        super(username, currentGame);
    }

    public void setResources(Map<Resource, Integer> resources) {
        this.mockResources = resources;
    }

    @Override
    public Map<Resource, Integer> getResources() {
        return Collections.unmodifiableMap(mockResources);
    }
}
