package it.polimi.ingsw.helpers;

import it.polimi.ingsw.network.rmi.RmiClient;

import java.rmi.RemoteException;
import java.util.Random;


public abstract class RmiClientFactory {
    private static int playerCount = 0;

    public static RmiClient getClient() {
        playerCount++;
        Random rn = new Random();
        RmiClient client = new RmiClient("localhost", 9091);
        try {
            client.checkCredentials("player" + rn.nextInt(), "password");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return client;
    }
}
