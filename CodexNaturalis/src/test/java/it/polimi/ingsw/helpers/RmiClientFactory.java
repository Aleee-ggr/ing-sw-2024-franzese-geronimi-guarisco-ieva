package it.polimi.ingsw.helpers;

import it.polimi.ingsw.network.rmi.RmiClient;

public abstract class RmiClientFactory {
    private static int playerCount = 0;

    public static RmiClient getClient() {
        playerCount++;
        System.out.println(playerCount);
        return new RmiClient("user_%d".formatted(playerCount), "pass_%d".formatted(playerCount),"localhost", 9090);
    }
}
