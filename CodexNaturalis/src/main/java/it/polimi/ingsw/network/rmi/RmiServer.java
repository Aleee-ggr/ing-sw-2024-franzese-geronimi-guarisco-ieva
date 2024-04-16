package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.threads.Shared;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.message.IdParser;
import it.polimi.ingsw.controller.threads.message.ThreadMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.network.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class RmiServer extends Server implements RmiServerInterface {
    private static final String name="rmiServer";
    private final Registry registry;


    public RmiServer(int port) throws RemoteException {
        System.out.println("Starting server...");
        RmiServerInterface stub = (RmiServerInterface) UnicastRemoteObject.exportObject(this, port);
        registry = LocateRegistry.createRegistry(1099);
        registry.rebind(name, stub);
        System.out.println("Started!");
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
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
        String message = ThreadMessage.draw.formatted(player, position);
        Shared<ThreadMessage> shared = threadMessages.get(game);
        //TODO check if player or game exists
        if (position < 0 || position > 5) {
            //TODO error handling
            return null;
        }
        shared.setValue(
                new ThreadMessage(Status.OK, message)
        );

        while (shared.getValue().status() == Status.OK);

        if (shared.getValue().status() == Status.RESPONSE) {
            return new IdParser().parse(shared.getValue().message());
        }
        return null;
    }

    @Override
    public boolean placeCard(UUID game, String player, Coordinates coordinates, Integer cardID) throws RemoteException {
        String message = ThreadMessage.place_card.formatted(player, coordinates.x(), coordinates.y(), cardID);
        synchronized (threadMessages) {
            Shared<ThreadMessage> shared = threadMessages.get(game);
            //TODO check if player or game exists
            shared.setValue(
                    new ThreadMessage(Status.OK, message)
            );

            while (shared.getValue().status() == Status.OK);

            return shared.getValue().status() != Status.ERROR;
        }
    }

    @Override
    public UUID newGame(Integer player_count) throws RemoteException {
        return createGame(player_count);
    }
}
