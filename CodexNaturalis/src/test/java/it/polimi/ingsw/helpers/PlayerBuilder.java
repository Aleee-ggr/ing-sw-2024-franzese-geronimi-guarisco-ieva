package it.polimi.ingsw.helpers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerBuilder {
    Map<Resource, Integer> resources = new HashMap<>();
    String username = "";

    public PlayerBuilder setResource(Resource resource, Integer quantity) {
        resources.put(resource, quantity);
        return this;
    }

    public PlayerBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public Player build() {
        MockPlayer p = new MockPlayer(username, new Game(new UUID(1,1))); //to check
        p.setResources(resources);
        return p;
    }
}
