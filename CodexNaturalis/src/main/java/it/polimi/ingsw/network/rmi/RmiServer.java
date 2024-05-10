package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.Server;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static final String name = "CodexServer";
    private final Registry registry;

    /**
     * Constructs a new RmiServer and sets up an RMI registry to allow remote clients to connect.
     * @param port The port on which the RMI server will listen.
     * @throws RemoteException If an RMI error occurs during server initialization.
     */
    public RmiServer(int port) throws RemoteException {
        System.out.println("Starting server...");
        try {
            System.out.println(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        RmiServerInterface stub = (RmiServerInterface) UnicastRemoteObject.exportObject(this, port);
        registry = LocateRegistry.createRegistry(port);
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

    public Integer getStartingCard(UUID game, String player) throws RemoteException {
        return getStartingCardServer(game, player);
    }

    @Override
    public HashMap<String, Integer> getScoreMap(UUID game, String name) throws RemoteException {
        return getScoreMapServer(game, name);
    }

    @Override
    public ArrayList<Integer> getHand(UUID game, String name) throws RemoteException {
        return getHandServer(game, name);
    }

    @Override
    public ArrayList<Integer> getCommonObjectives(UUID game, String name) throws RemoteException {
        return getCommonObjectivesServer(game, name);
    }

    @Override
    public HashMap<Resource, Integer> getPlayerResources(UUID game, String name, String nameRequiredData) throws RemoteException {
        return getPlayerResourcesServer(game, name, nameRequiredData);
    }

    @Override
    public ArrayList<String> getPlayers(UUID game, String name) throws RemoteException {
        return getPlayersServer(game, name);
    }

    @Override
    public GameState getGameState(UUID game, String name) throws RemoteException {
        return getGameStateServer(game, name);
    }

    @Override
    public ArrayList<Integer> getVisibleCards(UUID game, String name) throws RemoteException {
        return getVisibleCardsServer(game, name);
    }

    @Override
    public ArrayList<Integer> getBackSideDecks(UUID game, String name) throws RemoteException {
        return getBackSideDecksServer(game, name);
    }

    @Override
    public ArrayList<Coordinates> getValidPlacements(UUID game, String name) throws RemoteException {
        return getValidPlacementsServer(game, name);
    }

    @Override
    public HashMap<Coordinates, Integer> getBoard(UUID game, String name, String nameRequiredData) throws RemoteException {
        return getBoardServer(game, name, nameRequiredData);
    }

    @Override
    public boolean setStartingCard(UUID game, String username, boolean frontSideUp) throws RemoteException {
        return setStartingCardServer(game, username, frontSideUp);
    }

    @Override
    public ArrayList<Resource> getHandColor(UUID game, String name, String nameRequiredData) throws RemoteException {
        return getHandColorServer(game, name, nameRequiredData);
    }

    @Override
    public boolean choosePersonalObjective(UUID game, String username, Integer objectiveId) throws RemoteException {
        return choosePersonalObjectiveServer(game, username, objectiveId);
    }

    @Override
    public ArrayList<Integer> getStartingObjectives(UUID game, String username) throws RemoteException {
        return getStartingObjectivesServer(game, username);
    }

    @Override
    public boolean join(UUID game, String player) throws RemoteException {
        return joinGame(game, player);
    }

    public void wait(UUID game, String player) throws RemoteException{
        waitUpdate(game, player);
    }

    @Override
    public ArrayList<UUID> getAvailableGames(String username) throws RemoteException {
        return getAvailableGamesServer(username);
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
