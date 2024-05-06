package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.network.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

/**
 * The RmiServer class is an implementation of the RmiServerInterface, which defines methods for interacting with a remote game server using RMI.
 * This class manages game server operations such as drawing and placing cards, creating and joining games, and other game-related tasks.
 *
 * The class extends the Server class and utilizes RMI to provide remote access to the game server.
 * It sets up an RMI registry and binds the server to allow remote clients to invoke methods defined in the RmiServerInterface.
 *
 * Proper error handling and shutdown procedures are implemented to ensure smooth communication and resource management.
 * Runtime shutdown hooks are used to cleanly stop the server when the application exits.
 */
public class RmiServer extends Server implements RmiServerInterface {
    private static final String name = "rmiServer";
    private final Registry registry;

    /**
     * Constructs a new RmiServer and sets up an RMI registry to allow remote clients to connect.
     * @param port The port on which the RMI server will listen.
     * @throws RemoteException If an RMI error occurs during server initialization.
     */
    public RmiServer(int port) throws RemoteException {
        System.out.println("Starting server...");
        RmiServerInterface stub = (RmiServerInterface) UnicastRemoteObject.exportObject(this, port);
        registry = LocateRegistry.createRegistry(1099);
        registry.rebind(name, stub);
        System.out.println("Started!");
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    /**
     * Returns the name of the RMI server.
     * @return The name of the RMI server.
     */
    public static String getName() {
        return name;
    }

    /**
     * Stops the RMI server and unbinds the server from the registry.
     * It also sends a shutdown message to all game threads to clean up resources.
     */
    public void stop() {
        try {
            for (UUID key : threadMessages.keySet()) {
                threadMessages.get(key).add(
                    new ThreadMessage(
                            Status.REQUEST,
                            "",
                            "kill",
                            null,
                            UUID.randomUUID()
                    )
                );
            }
            registry.unbind(name);
        } catch (NotBoundException | RemoteException ignored) {}
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
    public boolean chooseStartingObjective(UUID game, String name, Integer objectiveId) throws RemoteException {
        return false;
    }

    @Override
    public String postChat(UUID game, String name, String message) throws RemoteException {
        return "";
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        return isValidPlayer(username, password);
    }
}
