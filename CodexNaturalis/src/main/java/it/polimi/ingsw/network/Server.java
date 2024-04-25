package it.polimi.ingsw.network;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Resource;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The abstract class Server serves as a base class for server implementations.
 * It manages games and their corresponding threads, as well as players and messages.
 * @author Daniele Ieva
 */
public abstract class Server {
    protected static final Map<UUID, BlockingQueue<ThreadMessage>> threadMessages = new ConcurrentHashMap<>();
    protected static final Map<UUID, Integer> games = new ConcurrentHashMap<>(); // TODO: remove game while closed
    protected static final Map<String, String> players = new ConcurrentHashMap<>();
    protected static final Map<String, UUID> playerGame = new ConcurrentHashMap<>();

    /**
     * Creates a new game with the specified number of players and starts its thread.
     * @param numberOfPlayers the number of players in the game (between 2 and 4 inclusive)
     * @return the unique ID of the created game, or null if the number of players is out of range
     */
    public static UUID createGame(int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 4) {
            return null;
        }
        UUID id = UUID.randomUUID();
        while (threadMessages.containsKey(id)) {
            id = UUID.randomUUID();
        }
        BlockingQueue<ThreadMessage> messageQueue = new LinkedBlockingDeque<>();
        threadMessages.put(id, messageQueue);
        new GameThread(messageQueue, numberOfPlayers).start();
        addGame(id, numberOfPlayers);
        return id;
    }

    /**
     * Check whether the given credentials are valid (size < 16 and username is not reused)
     * @param username the username of the player
     * @param password the password of the player
     * @return true if the given credentials are valid, false otherwise
     */
    public static boolean isValidPlayer(String username, String password) {
        if (username.length() > GameConsts.maxUsernameLength || password.length() > GameConsts.maxUsernameLength) {
            return false;
        }
        if (!players.containsKey(username)) {
            players.put(username, password);
            return true;
        }
        return players.get(username).equals(password);
    }

    /**
     * Gets an unmodifiable view of the games managed by the server.
     * @return a map of game IDs to the number of players in each game
     */
    public static Map<UUID, Integer> getGames() {
        return Collections.unmodifiableMap(games);
    }

    /**
     * Adds a game to the server's collection of games.
     * @param id the unique ID of the game
     * @param playerNum the number of players in the game
     */
    private static void addGame(UUID id, Integer playerNum) {
        games.put(id, playerNum);
    }

    /**
     * Sends a message to a specified game.
     * The method uses a synchronized block to safely interact with the message queue of the game.
     * @param game the unique ID of the game to send the message to
     * @param message the message to be sent to the game
     */
    public static void sendMessage(UUID game, ThreadMessage message ) {
        synchronized (threadMessages) {
            BlockingQueue<ThreadMessage> queue = threadMessages.get(game);
            UUID messageUUID = message.messageUUID();
            queue.add(message);

            ThreadMessage response;
            boolean responded = false;
            do {
                response = queue.peek();
                if (response != null && response.status() != Status.REQUEST) {

                    responded = response.messageUUID().equals(messageUUID);
                }
            } while (!responded);
        }
    }

    public static HashMap<String, Integer> getScoreMap(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getScoreMap(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        HashMap<String, Integer> scoreMap = new HashMap<>();

        if (response.args() != null) {
            for (String arg : response.args()) {
                String[] parts = arg.split(":");
                if (parts.length == 2) {
                    String key = parts[0];
                    Integer value = Integer.parseInt(parts[1]);
                    scoreMap.put(key, value);
                }
            }
        }

        return scoreMap;
    }

    public static ArrayList<Integer> getHand(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getHand(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        ArrayList<Integer> hand = new ArrayList<>();

        for (String arg : response.args()) {
            hand.add(Integer.parseInt(arg));
        }

        return hand;
    }

    public static void getPlayersBoards(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getPlayerBoards(
                username
        );
        sendMessage(game, message);
    }

    public static Integer[] getCommonObjectives(UUID game, String username){
        ThreadMessage message = ThreadMessage.getCommonObjectives(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        Integer[] commonObjectives = new Integer[response.args().length];
        for (int i = 0; i < response.args().length; i++) {
            commonObjectives[i] = Integer.parseInt(response.args()[i]);
        }

        return commonObjectives;
    }

    public static HashMap<Resource, Integer> getPlayerResources(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getPlayerResources(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        HashMap<Resource, Integer> playerResources = new HashMap<>();

        for (String arg : response.args()) {
            String[] parts = arg.split(":");
            if (parts.length == 2) {
                Resource resource = Resource.valueOf(parts[0]);
                Integer value = Integer.parseInt(parts[1]);
                playerResources.put(resource, value);
            }
        }

        return playerResources;
    }

    public static Integer[] getVisibleCards(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getVisibleCards(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        Integer[] visibleCards = new Integer[response.args().length];
        for (int i = 0; i < response.args().length; i++) {
            visibleCards[i] = Integer.parseInt(response.args()[i]);
        }

        return visibleCards;
    }

    public static Integer[] getBackSideDecks(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getBackSideDecks(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        Integer[] backSideDecks = new Integer[response.args().length];
        for (int i = 0; i < response.args().length; i++) {
            backSideDecks[i] = Integer.parseInt(response.args()[i]);
        }

        return backSideDecks;
    }

    public static Set<Coordinates> getValidPlacements(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getValidPlacements(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        Set<Coordinates> validPlacements = new HashSet<>();

        for (String arg : response.args()) {
            String[] parts = arg.split(",");
            validPlacements.add(new Coordinates(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }

        return validPlacements;
    }
}
