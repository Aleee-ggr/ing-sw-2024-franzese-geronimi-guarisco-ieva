package it.polimi.ingsw.network;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.Logger;
import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * The abstract class Server serves as a base class for server implementations.
 * It manages games and their corresponding threads, as well as players and messages.
 * Each protocol-specific implementation of a server calls for general methods in Server.java.
 * The class also contains a static initializer that starts a thread to monitor players disconnections.
 *
 * @see it.polimi.ingsw.network.rmi.RmiServer
 * @see it.polimi.ingsw.network.socket.SocketServer
 */
public abstract class Server {
    protected static final Map<UUID, BlockingQueue<ThreadMessage>> threadMessages = new ConcurrentHashMap<>();
    protected static final Map<UUID, Integer> games = new ConcurrentHashMap<>();
    protected static final Map<String, String> players = new ConcurrentHashMap<>();
    protected static final Map<String, AtomicInteger> playerStatus = new ConcurrentHashMap<>();
    protected static final Map<String, UUID> playerGame = new ConcurrentHashMap<>();
    protected static final Map<UUID, Map<String, WaitState>> gameTurns = new ConcurrentHashMap<>();
    protected static final Map<UUID, ArrayList<ChatMessage>> chat = new ConcurrentHashMap<>();
    protected static final Map<UUID, String> gameNames = new ConcurrentHashMap<>();
    protected static final Map<UUID, GameThread> gameThreads = new ConcurrentHashMap<>();

    /*
     * The static initializer starts a thread that monitors player disconnections.
     * The thread checks the status of each player every heartbeatInterval milliseconds.
     * If a player has been offline for more than disconnectionThreshold heartbeats, the player is considered disconnected.
     *
     * @see it.polimi.ingsw.GameConsts#disconnectionThreshold
     * @see it.polimi.ingsw.GameConsts#heartbeatInterval
     */
    static {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(GameConsts.heartbeatInterval);

                    if (playerStatus.isEmpty()) {
                        Thread.sleep(100);
                        continue;
                    }

                    for (Map.Entry<String, AtomicInteger> entry : playerStatus.entrySet()) {
                        if (entry.getValue().get() < 10) {
                            entry.getValue().incrementAndGet();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Gets an unmodifiable view of the games managed by the server.
     *
     * @return a map of game IDs to the number of players in each game
     */
    public static Map<UUID, Integer> getGames() {
        return Collections.unmodifiableMap(games);
    }

    /**
     * Gets the games available to a player.
     * The method iterates over the games and removes the ones that are closed or have all the players disconnected.
     *
     * @param username the username of the player
     * @return a map of game IDs to the names of the games available to the player
     */
    public static Map<UUID, String> getAvailableGamesServer(String username) {
        for (Map.Entry<UUID, GameThread> gameThread : gameThreads.entrySet()) {
            if (!gameThread.getValue().isAlive()) {
                UUID toRemove = gameThread.getKey();
                removeGame(toRemove, gameThread.getValue());
            } else {
                boolean disconnected = true;

                if (gameThread.getValue().getGameState().equals(GameState.LOBBY)) {
                    continue;
                }

                for (String player : gameThread.getValue().getPlayers()) {

                    if (!isOffline(player)) {
                        disconnected = false;
                        break;
                    }

                }

                if (disconnected) {
                    removeGame(gameThread.getKey(), gameThread.getValue());
                }

            }
        }
        return new HashMap<>(gameNames);
    }

    /**
     * Remove the game if it is closed or if all the players disconnect.
     *
     * @param game       the UUID of the game to remove
     * @param gameThread the GameThread of the game to remove
     */
    public static void removeGame(UUID game, GameThread gameThread) {
        threadMessages.remove(game);
        games.remove(game);
        gameTurns.remove(game);
        chat.remove(game);
        gameNames.remove(game);
        gameThreads.remove(game);
        gameThread.interrupt();
    }


    /**
     * General Server method to check if a player is offline.
     *
     * @param username the username of the player
     * @return true if the player is offline, false otherwise
     */
    public static boolean isOffline(String username) {
        if (username == null) {
            return false;
        }
        return playerStatus.get(username).get() > GameConsts.disconnectionThreshold;
    }

    /**
     * General Server method to create a new game with the specified number of players and starts its
     * thread.
     *
     * @param numberOfPlayers the number of players in the game (between 2 and 4
     *                        inclusive)
     * @return the unique ID of the created game, or null if the number of players
     * is out of range
     */
    public static UUID createGame(int numberOfPlayers, String gameName) {
        if (numberOfPlayers < 2 || numberOfPlayers > 4) {
            return null;
        }
        for (Map.Entry<UUID, String> entry : gameNames.entrySet()) {
            if (entry.getValue().equals(gameName)) {
                return null;
            }
        }
        UUID id = UUID.randomUUID();
        while (threadMessages.containsKey(id)) {
            id = UUID.randomUUID();
        }
        BlockingQueue<ThreadMessage> messageQueue = new LinkedBlockingDeque<>();
        threadMessages.put(id, messageQueue);
        gameTurns.put(id, new ConcurrentHashMap<>());
        gameNames.put(id, gameName);
        GameThread game = new GameThread(messageQueue, gameTurns.get(id), numberOfPlayers);
        gameThreads.put(id, game);
        game.start();
        addGame(id, numberOfPlayers);
        return id;
    }

    /**
     * General Server method to join a player to a game.
     *
     * @param game   the unique ID of the game to join
     * @param player the username of the player to join the game
     * @return true if the player was successfully joined to the game, false
     * otherwise
     */
    public static boolean joinGame(UUID game, String player) {
        if ((gameTurns.get(game).containsKey(player) && !isOffline(player)) || (playerGame.get(player) != null && playerGame.get(player) != game && !isOffline(player))) {
            return false;
        }
        playerGame.put(player, game);
        gameTurns.get(game).put(player, WaitState.WAIT);
        ThreadMessage message = ThreadMessage.join(player);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        return response.status() != Status.ERROR;
    }

    /**
     * General Server method to draw a card.
     *
     * @param game     the unique ID of the game
     * @param player   the username of the player drawing the card
     * @param position the index of the card to draw
     * @return the ID of the drawn card, or null if the card could not be drawn
     */
    public static Integer drawCardServer(UUID game, String player, Integer position) {
        ThreadMessage message = ThreadMessage.draw(player, position);

        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            return Integer.parseInt(response.args()[0]);
        } else {
            return null;
        }
    }

    /**
     * General Server method to place a card on the board.
     *
     * @param game        the unique ID of the game
     * @param player      the username of the player placing the card
     * @param coordinates the coordinates of the card placement
     * @param cardId      the ID of the card to place
     * @return true if the card was successfully placed, false otherwise
     */
    public static Boolean placeCardServer(UUID game, String player, Coordinates coordinates, Integer cardId) {
        ThreadMessage message = ThreadMessage.placeCard(player, coordinates, cardId);

        sendMessage(game, message);

        return threadMessages.get(game).remove().status() != Status.ERROR;
    }

    /**
     * General Server method to check whether the given credentials are valid (size less than 16 and username
     * is not reused)
     *
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
            playerStatus.put(username, new AtomicInteger(0));
            return true;
        } else {
            return players.get(username).equals(password);
        }
    }

    /**
     * Adds a game to the server's collection of games.
     *
     * @param id        the unique ID of the game
     * @param playerNum the number of players in the game
     */
    private static void addGame(UUID id, Integer playerNum) {
        games.put(id, playerNum);
    }

    /**
     * Sends a message to a specified game.
     * The method uses a synchronized block to safely interact with the message
     * queue of the game.
     *
     * @param game    the unique ID of the game to send the message to
     * @param message the message to be sent to the game
     */
    public static void sendMessage(UUID game, ThreadMessage message) {
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

    /**
     * General Server method to get the current state of a game.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player
     * @return the current state of the game
     */
    public static GameState getGameStateServer(UUID game, String username) {
        try {
            System.out.println("getGameState: " + gameThreads.get(game).getGameState());
            return gameThreads.get(game).getGameState();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * General Server method to get the players score map.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player requesting the score map
     * @return a map of player usernames to their scores
     */
    public static HashMap<String, Integer> getScoreMapServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getScoreMap(username);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        HashMap<String, Integer> scoreMap = new HashMap<>();

        if (response.status() == Status.OK) {
            for (String arg : response.args()) {
                String[] parts = arg.split(":");
                String key = parts[0];
                Integer value = Integer.parseInt(parts[1]);
                scoreMap.put(key, value);
            }
            return scoreMap;
        } else {
            return null;
        }
    }

    /**
     * General Server method to get the hand of cards of the main player.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player
     * @return a list of card IDs representing the player's hand
     */
    public static ArrayList<Integer> getHandServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getHand(username);
        return getIntegers(game, message);
    }

    /**
     * General Server method to set the starting card.
     *
     * @param game        the unique ID of the game
     * @param username    the username of the player setting the starting card
     * @param frontSideUp a boolean indicating if the card is placed front side up
     * @return true if the card was successfully set, false otherwise
     */
    public static boolean setStartingCardServer(UUID game, String username, boolean frontSideUp) {
        ThreadMessage message = ThreadMessage.placeStartingCard(username, String.valueOf(frontSideUp));
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        return response.status() != Status.ERROR;
    }

    /**
     * General Server method used by a player to choose his personal objective at the start of a game.
     *
     * @param game        the unique ID of the game
     * @param username    the username of the player choosing the personal objective
     * @param objectiveId the ID of the personal objective chosen by the player
     *                    the objective ID is chosen between the two options fetch by the player at the start of the game
     * @return true if the personal objective was successfully chosen, false otherwise
     */
    public static boolean choosePersonalObjectiveServer(UUID game, String username, Integer objectiveId) {
        ThreadMessage message = ThreadMessage.choosePersonalObjective(username, objectiveId);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        return Boolean.parseBoolean(response.args()[0]);
    }

    /**
     * General Server method used by a player to choose his color at the start of a game.
     *
     * @param game        the unique ID of the game
     * @param username    the username of the player choosing the color
     * @param playerColor the color chosen by the player
     * @return true if the color was successfully chosen, false otherwise
     */
    public static boolean choosePlayerColorServer(UUID game, String username, Color playerColor) {
        ThreadMessage message = ThreadMessage.choosePlayerColor(username, playerColor);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        return Boolean.parseBoolean(response.args()[0]);
    }

    /**
     * General Server method to get the hand colors of a player.
     * It's used to fetch the hand colors of the opponents to show the back of the cards to the player.
     *
     * @param game                 the unique ID of the game
     * @param username             the username of the player requesting the hand colors
     * @param usernameRequiredData the username of the player whose hand colors are requested
     * @return a list of card IDs representing the hand colors of the player
     */
    public static ArrayList<Resource> getHandColorServer(UUID game, String username, String usernameRequiredData) {
        ThreadMessage message = ThreadMessage.getHandColor(username, usernameRequiredData);
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

    /**
     * General Server method to get the hand types of a player.
     * It's used to fetch the hand types of the opponents to show the type of the cards to the player.
     *
     * @param game                 the unique ID of the game
     * @param username             the username of the player requesting the hand types
     * @param usernameRequiredData the username of the player whose hand types are requested
     * @return a list of booleans indicating if the cards are gold
     */
    public static ArrayList<Boolean> getHandTypeServer(UUID game, String username, String usernameRequiredData) {
        ThreadMessage message = ThreadMessage.getHandType(username, usernameRequiredData);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            ArrayList<Boolean> isGold = new ArrayList<>();

            for (String arg : response.args()) {
                isGold.add(Boolean.valueOf(arg));
            }

            return isGold;
        } else {
            return null;
        }
    }

    /**
     * General Server method to get the board of a player.
     *
     * @param game                 the unique ID of the game
     * @param username             the username of the player requesting the board
     * @param usernameRequiredData the username of the player whose board is requested
     * @return a map of coordinates to card IDs representing the player's board
     */
    public static HashMap<Coordinates, Integer> getBoardServer(UUID game, String username, String usernameRequiredData) {
        ThreadMessage message = ThreadMessage.getBoard(username, usernameRequiredData);
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

    /**
     * General Server method to get the placing order of a player.
     *
     * @param game                 the unique ID of the game
     * @param username             the username of the player requesting the placing order
     * @param usernameRequiredData the username of the player whose placing order is requested
     * @return a list of card IDs representing the placing order of the player
     */
    public static Deque<Integer> getPlacingOrderServer(UUID game, String username, String usernameRequiredData) {
        ThreadMessage message = ThreadMessage.getPlacingOrder(username, usernameRequiredData);
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

    /**
     * General Server method to fetch the common objectives of the game.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player requesting the common objectives
     * @return a list of objective IDs representing the common objectives of the game
     */
    public static ArrayList<Integer> getCommonObjectivesServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getCommonObjectives(username);
        return getIntegers(game, message);
    }

    /**
     * General Server method to fetch the personal objective of a player.
     *
     * @param username the username of the player requesting the personal objective
     * @param game     the unique ID of the game
     * @return the ID of the personal objective of the player
     */
    public static Integer getPersonalObjectiveServer(String username, UUID game) {
        ThreadMessage message = ThreadMessage.getPersonalObjective(username);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            return Integer.parseInt(response.args()[0]);
        } else {
            return null;
        }
    }

    /**
     * General Server method to fetch the available colors for a player to choose from.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player requesting the available colors
     * @return a list of colors representing the available colors for the player
     */
    public static ArrayList<Color> getAvailableColorsServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getAvailableColors(username);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            ArrayList<Color> availableColors = new ArrayList<>();
            for (String arg : response.args()) {
                availableColors.add(Color.valueOf(arg));
            }

            return availableColors;
        } else {
            return null;
        }
    }

    /**
     * General Server method to fetch the starting objectives for a player to choose from.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player requesting the starting objectives
     * @return a list of objective IDs representing the starting objectives of the player
     */
    public static ArrayList<Integer> getStartingObjectivesServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getStartingObjectives(username);
        return getIntegers(game, message);
    }

    /**
     * General Server method to fetch the players in a game.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player requesting the players
     * @return a list of usernames representing the players in the game
     */
    public static ArrayList<String> getPlayersServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getPlayers(username);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            return new ArrayList<>(List.of(response.args()));
        } else {
            return null;
        }

    }

    /**
     * General Server method to fetch the resources of a player.
     *
     * @param game                 the unique ID of the game
     * @param username             the username of the player requesting the resources
     * @param usernameRequiredData the username of the player whose resources are requested
     * @return a map of resources to their quantities
     */
    public static HashMap<Resource, Integer> getPlayerResourcesServer(UUID game, String username, String usernameRequiredData) {
        ThreadMessage message = ThreadMessage.getPlayerResources(username, usernameRequiredData);
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

    /**
     * General Server method to fetch the visible cards in a draw area of a player.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player requesting the visible cards
     * @return a list of card IDs representing the visible cards in the draw area
     */
    public static ArrayList<Integer> getVisibleCardsServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getVisibleCards(username);
        return getIntegers(game, message);
    }

    /**
     * General Server method to fetch the decks in a draw area of a player.
     * It's needed to show the player the backside color of the decks.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player requesting the decks
     * @return the ID of the first card of a deck in the draw area
     */
    public static ArrayList<Integer> getBackSideDecksServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getBackSideDecks(username);
        return getIntegers(game, message);
    }

    /**
     * Private Server method to manage the integers from a message.
     *
     * @param game    the unique ID of the game
     * @param message the message to fetch the integers from
     * @return a list of integers
     */
    private static ArrayList<Integer> getIntegers(UUID game, ThreadMessage message) {
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

    /**
     * General Server method to fetch the valid placements for cards on the board of the player.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player requesting the valid placements
     * @return a list of coordinates representing the valid placements for cards on the board
     */
    public static ArrayList<Coordinates> getValidPlacementsServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getValidPlacements(username);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            ArrayList<Coordinates> validPlacements = new ArrayList<>();

            for (String arg : response.args()) {
                String[] parts = arg.split(",");
                validPlacements.add(new Coordinates(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }

            return validPlacements;
        } else {
            return null;
        }
    }

    /**
     * General Server method to fetch a player color.
     *
     * @param game                 the unique ID of the game
     * @param username             the username of the player requesting the color
     * @param usernameRequiredData the username of the player whose color is requested
     * @return the color of the player
     */
    public static Color getPlayerColorServer(UUID game, String username, String usernameRequiredData) {
        ThreadMessage message = ThreadMessage.getPlayerColor(username, usernameRequiredData);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            return Color.valueOf(response.args()[0]);
        } else {
            return null;
        }
    }

    /**
     * General Server method for a player to get its starting card.
     * The method is used to fetch the starting card of the player at the start of the game.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player
     * @return the ID of the starting card of the player
     */
    public static Integer getStartingCardServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getStartingCard(username);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();

        if (response.status() == Status.OK) {
            return Integer.parseInt(response.args()[0]);
        } else {
            return null;
        }
    }

    /**
     * General Server method to get the string of player in turn.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player
     * @return the username of the player in turn
     */
    public static String getTurnPlayerServer(UUID game, String username) {
        ThreadMessage message = ThreadMessage.getTurnPlayer(username);
        sendMessage(game, message);
        ThreadMessage response = threadMessages.get(game).remove();
        if (response.status() == Status.OK) {
            return response.args()[0];
        } else {
            return null;
        }
    }

    /**
     * General Server method used by the client to wait updates from the server.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player
     * @return the current state of the player's turn
     */
    public static WaitState waitUpdate(UUID game, String username) {
        if (gameTurns.get(game).get(username) == WaitState.UPDATE) {
            gameTurns.get(game).put(username, WaitState.WAIT);
        }
        if (gameTurns.get(game).get(username) == WaitState.TURN_UPDATE) {
            gameTurns.get(game).put(username, WaitState.TURN);
        }
        while (gameTurns.get(game).get(username) == WaitState.WAIT || gameTurns.get(game).get(username) == WaitState.TURN) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return gameTurns.get(game).get(username);
    }

    /**
     * General Server method to reset the heartbeat of a player.
     *
     * @param username the username of the player
     */
    public static void heartbeatServer(UUID game, String username) {
        playerStatus.get(username).set(0);
    }

    /**
     * General Server method to fetch the chat messages of a player.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player requesting the chat messages
     * @return a list of chat messages
     */
    public static List<ChatMessage> fetchChatServer(UUID game, String username) {
        Predicate<ChatMessage> filter = ChatMessage.getPlayerFilter(username);
        if (chat.get(game) == null) {
            return new ArrayList<>();
        }
        return chat.get(game).stream().filter(filter).toList();
    }

    /**
     * General Server method to post a chat message.
     *
     * @param game     the unique ID of the game
     * @param username the username of the player posting the message
     * @param message  the message to be posted
     */
    public static void postChatServer(UUID game, String username, String message, String receiver) {
        chat.computeIfAbsent(game, k -> new ArrayList<>());
        chat.get(game).add(new ChatMessage(username, message, receiver));
        sendMessage(game, ThreadMessage.sendUpdate(username));
        threadMessages.get(game).remove();
    }
}
