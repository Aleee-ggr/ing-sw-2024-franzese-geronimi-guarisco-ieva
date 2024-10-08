package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.Server;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * The RmiServer class is an implementation of the RmiServerInterface, which defines methods for interacting with a remote game server using RMI.
 * This class manages game server operations such as drawing and placing cards, creating and joining games, and other game-related tasks.
 * <p>
 * The class extends the Server class and utilizes RMI to provide remote access to the game server.
 * It sets up an RMI registry and binds the server to allow remote clients to invoke methods defined in the RmiServerInterface.
 * <p>
 * Proper error handling and shutdown procedures are implemented to ensure smooth communication and resource management.
 * Runtime shutdown hooks are used to cleanly stop the server when the application exits.
 */
public class RmiServer extends Server implements RmiServerInterface {
    private static final String name = "CodexServer";
    private final Registry registry;

    /**
     * Constructs a new RmiServer and sets up an RMI registry to allow remote clients to connect.
     *
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
     *
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
                threadMessages.get(key).add(new ThreadMessage(Status.REQUEST, "", "kill", null, UUID.randomUUID()));
            }
            registry.unbind(name);
        } catch (NotBoundException | RemoteException ignored) {
        }
    }

    @Override
    public void ping(UUID game, String username) {
        heartbeatServer(game, username);
    }

    @Override
    public Integer drawCard(UUID game, String player, Integer position) throws RemoteException {
        return drawCardServer(game, player, position);
    }

    @Override
    public Boolean placeCard(UUID game, String player, Coordinates coordinates, Integer cardId) throws RemoteException {
        return placeCardServer(game, player, coordinates, cardId);
    }

    @Override
    public UUID newGame(Integer player_count, String gameName) throws RemoteException {
        return createGame(player_count, gameName);
    }

    @Override
    public Color getPlayerColor(UUID game, String name, String nameRequiredData) throws RemoteException {
        return getPlayerColorServer(game, name, nameRequiredData);
    }

    @Override
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
    public Integer getPersonalObjective(UUID game, String name) throws RemoteException {
        return getPersonalObjectiveServer(name, game);
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
    public Deque<Integer> getPlacingOrder(UUID game, String name, String nameRequiredData) throws RemoteException {
        return getPlacingOrderServer(game, name, nameRequiredData);
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
    public ArrayList<Boolean> getHandType(UUID game, String name, String nameRequiredData) throws RemoteException {
        return getHandTypeServer(game, name, nameRequiredData);
    }

    @Override
    public boolean choosePersonalObjective(UUID game, String username, Integer objectiveId) throws RemoteException {
        return choosePersonalObjectiveServer(game, username, objectiveId);
    }

    @Override
    public boolean choosePlayerColor(UUID game, String username, Color playerColor) throws RemoteException {
        return choosePlayerColorServer(game, username, playerColor);
    }

    @Override
    public ArrayList<Color> getAvailableColors(UUID game, String username) throws RemoteException {
        return getAvailableColorsServer(game, username);
    }

    @Override
    public ArrayList<Integer> getStartingObjectives(UUID game, String username) throws RemoteException {
        return getStartingObjectivesServer(game, username);
    }

    @Override
    public boolean join(UUID game, String player) throws RemoteException {
        return joinGame(game, player);
    }

    @Override
    public WaitState wait(UUID game, String player) throws RemoteException {
        return waitUpdate(game, player);
    }

    @Override
    public Map<UUID, String> getAvailableGames(String username) throws RemoteException {
        return getAvailableGamesServer(username);
    }

    @Override
    public String getTurnPlayer(UUID game, String name) throws RemoteException {
        return getTurnPlayerServer(game, name);
    }

    @Override
    public void postChat(UUID game, String sender, String message, String receiver) throws RemoteException {
        postChatServer(game, sender, message, receiver);
    }

    @Override
    public List<ChatMessage> fetchChat(UUID game, String username) throws RemoteException {
        return fetchChatServer(game, username);
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        return isValidPlayer(username, password);
    }
}
