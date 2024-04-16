package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.threads.Shared;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.message.IdParser;
import it.polimi.ingsw.controller.threads.message.ThreadMessage;
import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.player.Player;
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

    public Integer drawCard(UUID game, Player player, Integer position) throws ServerConnectionException {
        String message = ThreadMessage.draw_message.formatted(position);
        Shared<ThreadMessage> shared = threadMessages.get(game);
        if (position < 0 || position > 5) {
            //TODO error handling
            return null;
        }
        shared.setValue(
                new ThreadMessage(Status.OK, message)
        );

        while (shared.getValue().status() == Status.OK);

        return switch (shared.getValue().status()) {
            case RESPONSE -> new IdParser().parse(shared.getValue().message());
            default -> null;
        };
    }

    @Override
    public Card drawCard() throws ServerConnectionException {
        return null;
    }

    @Override
    public void placeCard() throws ServerConnectionException {

    }
}
