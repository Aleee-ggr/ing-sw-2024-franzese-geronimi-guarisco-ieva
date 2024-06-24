package it.polimi.ingsw.controller.threads;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Server;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.abs;

/**
 * GameThread Class, used to manage the thread-side logic for the controller.
 * Instantiate a new thread for each game to work simultaneously with the same
 * server.
 *
 * @author Daniele Ieva
 * @author ALessio Guarisco
 */
public class GameThread extends Thread {
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final Integer playerCount;
    private final Controller controller;
    private final Map<String, WaitState> turnMap;
    private final boolean running = true;
    private String currentPlayer;
    private volatile GameState gameState = GameState.LOBBY;

    /**
     * Constructor of GameThread
     *
     * @param messageQueue the queue of messages to be processed by the thread.
     * @param turnMap      the map of players and their turn status.
     * @param playerCount  the maximum number of players in the game.
     */
    public GameThread(BlockingQueue<ThreadMessage> messageQueue, Map<String, WaitState> turnMap, Integer playerCount) {
        this.messageQueue = messageQueue;
        this.playerCount = playerCount;
        this.controller = new Controller(messageQueue, playerCount);
        this.turnMap = turnMap;
    }

    /**
     * Getter for the game state.
     *
     * @return the current game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Getter for the players in the game.
     *
     * @return the list of players in the game.
     */
    public List<String> getPlayers() {
        return controller.getGame().getPlayers().stream().map(Player::getUsername).toList();
    }

    /**
     * Run method of the thread, it manages the game loop.
     */
    @Override
    public void run() {
        gameLoop();
    }

    /**
     * GameLoop method, manages the game state and the game flow.
     * It determines the game state and calls the appropriate method in GameThread.
     * It is a loop that runs until the game is stopped.
     */
    public void gameLoop() {
        while (gameState != GameState.STOP) {
            switch (gameState) {
                case LOBBY:
                    gameLobby();
                    break;
                case SETUP:
                    controller.randomizePlayers();
                    setup();
                    break;
                case MAIN:
                    mainGame();
                    break;
                case ENDGAME:
                    endGame();
                    break;
            }
        }
        gameStop();
    }

    /**
     * GameLobby method, manages the game lobby state.
     * It waits for the game to be full and then changes the game state to SETUP.
     */
    public void gameLobby() {
        Thread lobby = new Thread(() -> {
            while (gameState == GameState.LOBBY) {
                ThreadMessage msg = getMessage();
                if (GameState.lobby.contains(msg.type())) {
                    respond(msg);
                } else {
                    messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), ("Invalid message for " + "LOBBY:%s").formatted(msg.type())));
                }
                if (controller.getGame().getPlayers().size() == playerCount) {
                    gameState = GameState.SETUP;
                    controller.getGame().setGameState(GameState.SETUP);
                }
            }
        });
        Thread timer = new Thread(() -> {
            try {
                sleep(10 * 1000 * 60);
                gameState = GameState.STOP;
                controller.getGame().setGameState(GameState.STOP);
            } catch (InterruptedException ignored) {
            }
        });
        lobby.start();
        timer.start();
        while (gameState == GameState.LOBBY) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        timer.interrupt();
        lobby.interrupt();
    }

    /**
     * Setup method, manages the game setup state.
     * It waits for all players to choose their personal objectives and starting
     * cards.
     */
    public void setup() {
        for (String currentPlayer : controller.getGame().getPlayers().stream().map(Player::getUsername).toList()) {
            boolean objChosen = false;
            boolean startChosen = false;
            boolean colorChosen = false;
            this.currentPlayer = currentPlayer;
            turnMap.put(currentPlayer, WaitState.SETUP_TURN);

            controller.getGame().getPlayers().stream().filter(p -> p.getUsername().equals(currentPlayer)).toList().getFirst().drawFirstHand();

            while (!objChosen || !startChosen || !colorChosen) {

                ThreadMessage msg = getMessage();

                if (Server.isOffline(currentPlayer)) {
                    disconnectionHandler(objChosen, startChosen, colorChosen);
                    break;
                }

                if(msg == null) {
                    continue;
                }

                if (msg.type().contains("get") || msg.type().contains("join")) {
                    respond(msg);
                    continue;
                }

                if (GameState.setup.contains(msg.type()) && msg.player().equals(currentPlayer)) {
                    respond(msg);
                } else if (!msg.player().equals(currentPlayer)) {
                    messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "not player's turn."));
                } else {
                    messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "Invalid message for context: %s".formatted(msg.type())));
                }

                if (msg.type().equals("choosePersonalObjective")) {
                    objChosen = true;
                } else if (msg.type().equals("placeStartingCard")) {
                    startChosen = true;
                } else if (msg.type().equals("choosePlayerColor")) {
                    colorChosen = true;
                }
            }
            turnMap.put(currentPlayer, WaitState.WAIT);
        }
        gameState = GameState.MAIN;
        controller.getGame().setGameState(GameState.MAIN);
    }

    /**
     * MainGame method, manages the main game state.
     * It waits for all players to place a card and draw a card.
     * It repeats until a player reaches the ending score or the cards are over.
     * It calls the playerTurn method for each player.
     */
    public void mainGame() {
        for (Player player : controller.getGame().getPlayers()) {
            this.currentPlayer = player.getUsername();
            turnMap.put(player.getUsername(), WaitState.TURN_UPDATE);
            if (playerTurn(player.getUsername()) || controller.getGame().getGameBoard().areCardsOver()) {
                gameState = GameState.ENDGAME;
                controller.getGame().setGameState(GameState.ENDGAME);
            }
            turnMap.put(player.getUsername(), WaitState.WAIT);
        }
    }

    /**
     * PlayerTurn method, manages the player turn.
     * It waits for the player to place a card and draw a card.
     *
     * @param playerName the name of the player whose turn it is.
     * @return true if the player has reached the ending score, false otherwise.
     */
    public boolean playerTurn(String playerName) {
        boolean draw = false;
        boolean place = false;

        if (Server.isOffline(currentPlayer)) {
            disconnectionHandler(place, draw, true);
            return false;
        }

        while (!place) {
            ThreadMessage msg = getMessage();

            if (msg == null) {
                disconnectionHandler(place, draw, true);
                return false;
            }

            if (msg.type().contains("get") || msg.type().contains("join") || msg.type().equals("sendUpdate")) {
                respond(msg);
                continue;
            }

            if (msg.type().equals("place") && msg.player().equals(playerName)) {
                place = respond(msg);
                if (place) {
                    sendUpdate();
                }
                continue;
            }

            messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "Invalid message for context PLACE: %s".formatted(msg.type())));
        }

        while (!draw && !controller.getGame().getGameBoard().areCardsOver()) {
            ThreadMessage msg = getMessage();

            if (msg == null) {
                disconnectionHandler(place, draw, true);
                return false;
            }

            if (msg.type().contains("get") || msg.type().contains("join") || msg.type().equals("sendUpdate")) {
                respond(msg);
                continue;
            }

            if (msg.type().equals("draw") && msg.player().equals(playerName)) {
                draw = respond(msg);
                if (draw) {
                    sendUpdate();
                }
                continue;
            }

            messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "Invalid message for context DRAW: %s".formatted(msg.type())));
        }

        Player player = controller.getGame().getPlayers().stream().filter(p -> p.getUsername().equals(playerName)).toList().getFirst();
        return (player.getScore() >= GameConsts.endingScore);
    }

    /**
     * EndGame method, manages the end game state.
     * It calculates the final score of each player and sends it to the clients.
     */
    public void endGame() {
        for (String currentPlayer : controller.getGame().getPlayers().stream().map(Player::getUsername).toList()) {
            this.currentPlayer = currentPlayer;
            turnMap.put(currentPlayer, WaitState.TURN_UPDATE);
            playerTurn(currentPlayer);
            turnMap.put(currentPlayer, WaitState.WAIT);
        }

        for (Player player : controller.getGame().getPlayers()) {
            for (Objective obj : controller.getGame().getGameBoard().getGlobalObjectives()) {
                controller.getGame().getGameBoard().updateScore(player, obj.getPoints(player));
            }
            controller.getGame().getGameBoard().updateScore(player, player.getHiddenObjective().getPoints(player));
        }

        gameState = GameState.STOP;
        controller.getGame().setGameState(GameState.STOP);
    }

    /**
     * Initiates the process to stop the game, updates player states, and processes messages until timeout.
     * <p>
     * This method sets the game state to end for all players, starts a new thread to process incoming messages,
     * and waits for a specified duration before stopping message processing. During this time, it responds to
     * messages of type "get" and logs errors for other message types.
     */
    public void gameStop() {
        for (Player player : controller.getGame().getPlayers()) {
            turnMap.put(player.getUsername(), WaitState.ENDGAME);
        }

        final AtomicBoolean running = new AtomicBoolean(true);

        new Thread(() -> {
            while (running.get()) {
                ThreadMessage msg = getMessage();
                if (msg == null) {
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                if (msg.type().contains("get")) {
                    respond(msg);
                } else {
                    messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), ("Invalid message for " + "context STOP: %s").formatted(msg.type())));
                }
            }
        }).start();
        try {
            sleep(1000 * 60);
        } catch (InterruptedException ignored) {
        }

        running.set(false);
    }

    /**
     * Handles disconnection scenarios for the current player in the game.
     * <p>
     * This method adjusts game state and player actions based on the current game state and the parameters
     * indicating which actions were completed by the player before disconnection.
     *
     * @param firstParam  Indicates if the player has chosen the personal objective.
     * @param secondParam Indicates if the player has drawn the starting card.
     * @param thirdParam  Indicates if the player has chosen a player color.
     */
    private void disconnectionHandler(Boolean firstParam, Boolean secondParam, Boolean thirdParam) {
        System.out.printf("Player %s is offline%n", currentPlayer);
        Player user = controller.getGame().getPlayers().stream().filter(p -> p.getUsername().equals(currentPlayer)).toList().getFirst();

        switch (gameState) {
            case SETUP:
                Random rand = new Random();

                if (!firstParam) { // if the player has not chosen the personal objective
                    int index = rand.nextInt() % 2;

                    user.setStartingObjectives();

                    ArrayList<Objective> startingObjectives = user.getStartingObjectives();
                    user.choosePersonalObjective(startingObjectives.get(abs(index)).getId());
                }

                if (!secondParam) {
                    if (user.getDrawnStartingCard() == null) {
                        user.drawStartingCard();
                    }
                    user.setFirstCard(rand.nextBoolean());
                }

                if (!thirdParam) {
                    if (user.getPlayerColor() == null) {
                        ArrayList<Color> availableColors = user.getAvailableColors();
                        user.choosePlayerColor(availableColors.get(rand.nextInt(availableColors.size())));
                    }
                }

                break;
            case MAIN:
                turnMap.put(currentPlayer, WaitState.TURN);
                if (!firstParam) { // if the player has not placed the card
                    return;
                }
                if (!secondParam) { // if the player has not drawn the card
                    if (user.drawDecks(false) == null) {
                        user.drawVisible(0);
                    }
                }
                break;
            case ENDGAME:
                break;
        }
    }

    /**
     * GetMessage method, gets the next message from the queue.
     * It waits until a message with status REQUEST is available.
     *
     * @return the next message in the queue.
     */
    private ThreadMessage getMessage() {
        ThreadMessage msg;
        do {
            msg = messageQueue.peek();
        } while ((msg == null || msg.status() != Status.REQUEST) && !Server.isOffline(currentPlayer));

        if (Server.isOffline(currentPlayer)) {
            return null;
        }

        return messageQueue.remove();
    }

    /**
     * Respond method, processes the message and calls the appropriate method in the
     * controller.
     *
     * @param msg the message to be processed.
     * @return the message itself.
     */
    private boolean respond(ThreadMessage msg) {
        switch (msg.type()) {
            case "create":
                controller.createGame(msg.player(), Integer.valueOf(msg.args()[0]), UUID.fromString(msg.args()[1]), msg.messageUUID());
                break;
            case "join":
                controller.join(msg.player(), msg.messageUUID());
                break;
            case "place":
                return controller.placeCard(msg.player(), new Coordinates(Integer.valueOf(msg.args()[0]), Integer.valueOf(msg.args()[1])), Integer.valueOf(msg.args()[2]), msg.messageUUID());
            case "update":
                controller.update(msg.player(), currentPlayer.equals(msg.player()), msg.messageUUID());
                break;
            case "draw":
                return controller.draw(msg.player(), Arrays.stream(msg.args()).mapToInt(Integer::parseInt).findFirst().orElse(-1), msg.messageUUID());
            case "choosePersonalObjective":
                controller.choosePersonalObjective(msg.player(), Integer.valueOf(msg.args()[0]), msg.messageUUID());
                break;
            case "choosePlayerColor":
                controller.choosePlayerColor(msg.player(), Color.valueOf(msg.args()[0]), msg.messageUUID());
                break;
            case "getScoreMap":
                controller.getScoreMap(msg.player(), msg.messageUUID());
                break;
            case "getHand":
                controller.getHand(msg.player(), msg.messageUUID());
                break;
            case "getCommonObjectives":
                controller.getCommonObjectives(msg.player(), msg.messageUUID());
                break;
            case "getPersonalObjective":
                controller.getPersonalObjective(msg.player(), msg.messageUUID());
                break;
            case "getStartingObjectives":
                controller.getStartingObjectives(msg.player(), msg.messageUUID());
                break;
            case "getAvailableColors":
                controller.getAvailableColors(msg.player(), msg.messageUUID());
                break;
            case "getPlayerResources":
                controller.getPlayerResources(msg.player(), msg.args()[0], msg.messageUUID());
                break;
            case "getPlayerColor":
                controller.getPlayerColor(msg.player(), msg.args()[0], msg.messageUUID());
                break;
            case "getVisibleCards":
                controller.getVisibleCards(msg.player(), msg.messageUUID());
                break;
            case "getBackSideDecks":
                controller.getBackSideDecks(msg.player(), msg.messageUUID());
                break;
            case "getValidPlacements":
                controller.getValidPlacements(msg.player(), msg.messageUUID());
                break;
            case "getBoard":
                controller.getBoard(msg.player(), msg.args()[0], msg.messageUUID());
                break;
            case "getHandColor":
                controller.getHandColor(msg.player(), msg.args()[0], msg.messageUUID());
                break;
            case "getHandType":
                controller.getHandType(msg.player(), msg.args()[0], msg.messageUUID());
                break;
            case "getPlacingOrder":
                controller.getPlacingOrder(msg.player(), msg.args()[0], msg.messageUUID());
                break;
            case "getPlayers":
                controller.getPlayers(msg.player(), msg.messageUUID());
                break;
            case "getStartingCard":
                controller.getStartingCards(msg.player(), msg.messageUUID());
                break;
            case "placeStartingCard":
                controller.placeStartingCard(msg.player(), Boolean.parseBoolean(msg.args()[0]), msg.messageUUID());
                break;
            case "getGameState":
                controller.getGameState(msg.player(), msg.messageUUID());
                break;
            case "sendUpdate":
                sendUpdate();
                messageQueue.add(ThreadMessage.okResponse(msg.player(), msg.messageUUID()));
                break;
            case "getTurnPlayer":
                messageQueue.add(ThreadMessage.getTurnPlayerResponse(msg.player(), msg.messageUUID(), currentPlayer));
                break;
            case "kill":
                gameState = GameState.STOP;
                break;
            default:
                messageQueue.add(new ThreadMessage(Status.ERROR, msg.player(), "unknown", new String[]{msg.type()}, msg.messageUUID()));
        }
        return false;
    }

    /**
     * Sends game state updates to players based on the current player and their respective states.
     * <p>
     * This method updates the turnMap for each player in the game, indicating whether each player
     * needs a turn update or a general game state update.
     * </p>
     * <p>
     * If currentPlayer is not null, it sets its corresponding entry in turnMap to
     * TURN_UPDATE. All other players have their entries set to UPDATE.
     * </p>
     */
    public void sendUpdate() {
        System.out.println(currentPlayer);
        if (currentPlayer != null) {
            turnMap.put(currentPlayer, WaitState.TURN_UPDATE);
        }
        for (Player player : controller.getGame().getPlayers()) {
            if (!player.getUsername().equals(currentPlayer)) {
                turnMap.put(player.getUsername(), WaitState.UPDATE);
            }
        }
    }
}
