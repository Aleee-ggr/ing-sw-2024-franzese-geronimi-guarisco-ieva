package it.polimi.ingsw.helpers;

import it.polimi.ingsw.network.rmi.RmiClient;


//TODO: add usernames after refactor
public abstract class RmiClientFactory {
    private static int playerCount = 0;

    public static RmiClient getClient() {
        playerCount++;
        return new RmiClient("localhost", 9090);
    }
}
