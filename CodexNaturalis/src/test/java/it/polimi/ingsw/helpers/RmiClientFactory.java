package it.polimi.ingsw.helpers;

import it.polimi.ingsw.network.rmi.RmiClient;

import java.rmi.RemoteException;


//TODO: add usernames after refactor
public abstract class RmiClientFactory {
    private static int playerCount = 0;

    public static RmiClient getClient() {
        playerCount++;
        RmiClient client = new RmiClient("localhost", 9090);
        try {
            client.checkCredentials("player", "password");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return client;
    }
}
