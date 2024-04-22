package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.message.ThreadMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.network.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import java.util.UUID;

public class RmiServer extends Server implements RmiServerInterface {
    private static final String name = "rmiServer";
    private final Registry registry;


    public RmiServer(int port) throws RemoteException {
        System.out.println("Starting server...");
        RmiServerInterface stub = (RmiServerInterface) UnicastRemoteObject.exportObject(this, port);
        registry = LocateRegistry.createRegistry(1099);
        registry.rebind(name, stub);
        System.out.println("Started!");
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public static String getName() {
        return name;
    }

    public void stop() {
        try {
            registry.unbind(name);
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer drawCard(UUID game, String player, Integer position) throws RemoteException {
        ThreadMessage message = ThreadMessage.draw(
                player,
                position
        );

        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() != Status.OK) {
            return null;
        }
        return Integer.parseInt(response.args()[0]);
    }

    @Override
    public boolean placeCard(UUID game, String player, Coordinates coordinates, Integer cardId) throws RemoteException {
        ThreadMessage message = ThreadMessage.placeCard(
                player,
                coordinates,
                cardId
        );

        sendMessage(game, message);

        return threadMessages.get(game).remove().status() != Status.ERROR;
    }

    @Override
    public UUID newGame(Integer player_count) throws RemoteException {
        return createGame(player_count);
    }

    @Override
    public boolean join(UUID game, String player) throws RemoteException {
        ThreadMessage message = ThreadMessage.join(
                player
        );

        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        return response.status() != Status.ERROR;
    }

    //TODO: methods to implement
    @Override
    public Integer[] getStartingObjectives(UUID game, String name) throws RemoteException {
        return new Integer[0];
    }

    @Override
    public boolean chooseStartingObjective(UUID game, String name, Integer objectiveId) throws RemoteException {
        return false;
    }

    @Override
    public String postChat(UUID game, String name, String message) throws RemoteException {
        return "";
    }

    @Override
    public void waitUpdate(UUID game, String name) throws RemoteException {

    }

    @Override
    public Set<Coordinates> getPlacementCoordinates(UUID game, String name) throws RemoteException{
        return null;
    }
}
