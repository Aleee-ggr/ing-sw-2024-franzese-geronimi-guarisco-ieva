package it.polimi.ingsw.network;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.Logger;
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
    protected static final Map<UUID, Map<String, Boolean>> gameTurns = new ConcurrentHashMap<>();

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
        gameTurns.put(id, new ConcurrentHashMap<>());
        new GameThread(messageQueue, gameTurns.get(id), numberOfPlayers).start();
        addGame(id, numberOfPlayers);
        return id;
    }

    /**
     * Joins a player to a game.
     * @param game the unique ID of the game to join
     * @param player the username of the player to join the game
     * @return true if the player was successfully joined to the game, false otherwise
     */
    public static boolean joinGame(UUID game, String player){
        gameTurns.get(game).put(player, false);
        ThreadMessage message = ThreadMessage.join(
                player
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        return response.status() != Status.ERROR;
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
            Logger.log(message);

            ThreadMessage response;
            boolean responded = false;
            do {
                response = queue.peek();
                if (response != null && response.status() != Status.REQUEST) {
                    responded = response.messageUUID().equals(messageUUID);
                    Logger.log(response);
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

        if (response.status() == Status.OK) {
            ArrayList<Integer> hand = new ArrayList<>();

            for (String arg : response.args()) {
                hand.add(Integer.parseInt(arg));
            }

            return hand;
        } else {
            return null;
        }
    }

    public static boolean choosePersonalObjective(UUID game, String username, Integer objectiveId) {
        ThreadMessage message = ThreadMessage.choosePersonalObjective(
                username,
                objectiveId
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        return Boolean.parseBoolean(response.args()[0]);
    }

    public static ArrayList<Resource> getHandColor(UUID game, String username, String usernameRequiredData) {
        ThreadMessage message = ThreadMessage.getHandColor(
                username,
                usernameRequiredData
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            ArrayList<Resource> handColor = new ArrayList<>();

            for (String arg : response.args()) {
                handColor.add(Resource.valueOf(arg));
            }

            return handColor;
        } else {
            return null;
        }
    }

    public static HashMap<Coordinates, Integer> getBoard(UUID game, String username, String usernameRequiredData) {
        ThreadMessage message = ThreadMessage.getBoard(
                username,
                usernameRequiredData
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            HashMap<Coordinates, Integer> board = new HashMap<>();

            for (String arg : response.args()) {
                String[] parts = arg.split(",");
                Coordinates coordinates = new Coordinates(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                board.put(coordinates, Integer.parseInt(parts[2]));
            }

            return board;
        } else {
            return null;
        }
    }

    public static Deque<Integer> getLastPlacedCards(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getLastPlacedCards(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            Deque<Integer> cards = new ArrayDeque<>();

            for (String arg : response.args()) {
                cards.add(Integer.parseInt(arg));
            }

            return cards;
        } else {
            return null;
        }
    }

    public static ArrayList<Integer> getCommonObjectives(UUID game, String username){
        ThreadMessage message = ThreadMessage.getCommonObjectives(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            ArrayList<Integer> commonObjectives = new ArrayList<>();
            for (String arg : response.args()) {
                commonObjectives.add(Integer.parseInt(arg));
            }

            return commonObjectives;
        } else {
            return null;
        }
    }

    public static ArrayList<Integer> getStartingObjectives(UUID game, String username){
        ThreadMessage message = ThreadMessage.getStartingObjectives(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            ArrayList<Integer> startingObjectives = new ArrayList<>();
            for (String arg : response.args()) {
                startingObjectives.add(Integer.parseInt(arg));
            }

            return startingObjectives;
        } else {
            return null;
        }
    }

    public static ArrayList<String> getPlayers(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getPlayers(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            ArrayList<String> players = new ArrayList<>();
            Collections.addAll(players, response.args());

            return players;
        } else {
            return null;
        }

    }

    public static HashMap<Resource, Integer> getPlayerResources(UUID game, String username, String usernameRequiredData) {
        ThreadMessage message = ThreadMessage.getPlayerResources(
                username,
                usernameRequiredData
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
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
        } else {
            return null;
        }
    }

    public static ArrayList<Integer> getVisibleCards(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getVisibleCards(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            ArrayList<Integer> visibleCards = new ArrayList<>();
            for (String arg : response.args()) {
                visibleCards.add(Integer.parseInt(arg));
            }

            return visibleCards;
        } else {
            return null;
        }
    }

    public static ArrayList<Integer> getBackSideDecks(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getBackSideDecks(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            ArrayList<Integer> backSideDecks = new ArrayList<>();
            for (String arg : response.args()) {
                backSideDecks.add(Integer.parseInt(arg));
            }

            return backSideDecks;
        } else {
            return null;
        }
    }

    public static Set<Coordinates> getValidPlacements(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getValidPlacements(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            Set<Coordinates> validPlacements = new HashSet<>();

            for (String arg : response.args()) {
                String[] parts = arg.split(",");
                validPlacements.add(new Coordinates(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }

            return validPlacements;
        } else {
            return null;
        }
    }
    public static Integer getStartingCard(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getStartingCard(
                username
        );
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            return Integer.parseInt(response.args()[0]);
        } else {
            return null;
        }
    }

    public static void waitUpdate(UUID game, String username) {
        while(!gameTurns.get(game).get(username)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


