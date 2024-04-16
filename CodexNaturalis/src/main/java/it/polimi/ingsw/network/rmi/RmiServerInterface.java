package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.model.board.Coordinates;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface RmiServerInterface extends Remote {
    public Integer drawCard(UUID game, String player, Integer position) throws RemoteException;

    public Status placeCard(UUID game, String player, Coordinates coordinates, Integer cardID) throws RemoteException;
}
