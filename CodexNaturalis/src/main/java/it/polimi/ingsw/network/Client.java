package it.polimi.ingsw.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.helpers.exceptions.model.ElementNotInHand;
import it.polimi.ingsw.helpers.exceptions.model.HandFullException;
import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.client.ClientData;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;

import java.io.IOException;
import java.util.*;

/**
 * Client class, baseline for building a client that can connect to a server to participate in a game.
 * Contains the generic methods that a client with any protocol can use to interact with the server.
 * The client can be extended to implement specific methods for each protocol.
 */
public class Client {
    protected final String serverAddress;
    protected final int serverPort;

    protected String username;
    protected String password;

    protected UUID gameId;
    protected String turnPlayerName;
    protected GameState gameState;
    protected Map<UUID, String> availableGames;
    protected int playerNum = 0;
    protected ArrayList<String> players;
    protected HashMap<String, ClientData> playerData;
    protected HashMap<String, Integer> scoreMap;

    protected ArrayList<Card> visibleCards;
    protected ArrayList<Card> backSideDecks;
    protected List<ChatMessage> chat;


    /**
     * Constructs a new Client object with the specified server address, and server port.
     *
     * @param serverAddress The address of the server.
     * @param serverPort    The port of the server.
     */
    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.availableGames = new HashMap<>();
        this.players = new ArrayList<>();
        this.scoreMap = new HashMap<>();
        this.playerData = new HashMap<>();
        this.gameState = GameState.LOBBY;
    }

    /**
     * Method to start heartbeat in the various clients
     *
     * @param client the client that will send the heartbeat
     */
    public static void startHeartBeat(final ClientInterface client) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(GameConsts.heartbeatInterval);
                    client.pingServer();
                } catch (IOException | InterruptedException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            }
        }).start();
    }

    /**
     * Getter for the username of the client.
     *
     * @return a String representing the username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the GameState.
     *
     * @return the GameState of the game.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Getter for the available games.
     *
     * @return an ArrayList of UUIDs of the available games.
     */
    public Map<UUID, String> getAvailableGames() {
        return this.availableGames;
    }

    /**
     * Getter for the player number.
     *
     * @return an integer representing the number of players of the game.
     */
    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * Getter for the players.
     *
     * @return an ArrayList of Strings representing the players of the game.
     */
    public ArrayList<String> getPlayers() {
        return this.players;
    }

    /**
     * Getter for the opponents' data.
     *
     * @return a HashMap of Strings and ClientData representing the opponents' data.
     */
    public HashMap<String, ClientData> getOpponentData() {
        return this.playerData;
    }

    /**
     * Getter for the score map.
     *
     * @return a HashMap of Strings and Integers representing the score of each player.
     */
    public HashMap<String, Integer> getScoreMap() {
        return this.scoreMap;
    }

    /**
     * Getter for the visible cards.
     *
     * @return an ArrayList of Cards representing the visible cards.
     */
    public ArrayList<Card> getVisibleCards() {
        return visibleCards;
    }

    /**
     * Getter for the back side of the decks.
     *
     * @return an ArrayList of Cards representing the back side of the decks.
     */
    public ArrayList<Card> getDecksBacks() {
        return backSideDecks;
    }

    /**
     * Getter for the playerData.
     *
     * @return a PlayerData class of the client player.
     */
    public PlayerData getPlayerData() {
        return (PlayerData) playerData.get(username);
    }

    /**
     * Getter for the chat.
     *
     * @return a List of ChatMessages representing the chat.
     */
    public List<ChatMessage> getChat() {
        return this.chat;
    }

    /**
     * Setter for the gameId.
     *
     * @param gameId the UUID of the game.
     */
    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    /**
     * Setter for the credentials of the player.
     *
     * @param username the username of the player.
     * @param password the password of the player.
     */
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * This method overrides the toString method for the Client class.
     * It constructs a string representation of the Client object, including the username and the data of all players.
     *
     * @return a String representing the Client object.
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Username: ").append(this.username).append("\n");
        for (String p : players) {
            out.append(p).append(":\n");
            out.append(playerData.get(p).toString());
        }
        return out.toString();
    }

    /**
     * Method used to create the player data.
     * It creates the player data for the client player and the opponent data for the other players.
     * Mainly useful for testing.
     *
     * @param players an ArrayList of Strings representing the players of the game.
     */
    public void createPlayerData(ArrayList<String> players) {
        for (String player : players) {
            if (player.equals(this.username)) {
                playerData.put(player, new PlayerData());
            } else {
                playerData.put(player, new OpponentData());
            }
            this.scoreMap.put(player, 0);
        }

        this.players = players;
        this.playerNum = players.size();
    }

    /**
     * Method used for drawing a card.
     * It receives the id of the drawn card from the server and adds the card to the hand of the player in the model of the Client.
     *
     * @param id an Integer representing the id of the drawn card.
     * @return a boolean value representing the success of the local operation.
     */
    public boolean drawCardClient(Integer id) {
        if (id != null) {
            try {
                ((PlayerData) playerData.get(username)).addToHand(Game.getCardByID(id));
            } catch (HandFullException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    /**
     * Method used for placing a card.
     * It receives the id of the placed card from the user interface and removes the card from the hand of the player in the model of the Client.
     *
     * @param success a boolean value representing the success of the place from the server.
     * @param cardId  an Integer representing the id of the placed card.
     * @return a boolean value representing the success of the local operation.
     */
    public boolean placeCardClient(Boolean success, int cardId) {
        if (success) {
            try {
                ((PlayerData) playerData.get(username)).removeFromHand(Game.getCardByID(cardId));
            } catch (ElementNotInHand e) {
                throw new RuntimeException(e);
            }

            return true;
        }
        return false;
    }

    /**
     * Method used for placing the Starting Card on the board.
     * It receives the front side of the card from the user interface and the success of the operation from the server.
     *
     * @param frontSideUp a boolean value representing the front side of the card.
     * @param success     a boolean value representing the success of the operation from the server.
     * @return a boolean value representing the success of the placement locally.
     */
    public boolean placeStartingCardClient(Boolean frontSideUp, Boolean success) {
        if (success) {
            getPlayerData().setStartingCard((StartingCard) getPlayerData().getStartingCard().setFrontSideUp(frontSideUp));
        }
        return success;
    }

    /**
     * Method used for choosing the personal objective.
     * It receives the id of the chosen objective from the user interface and the success of the operation from the server.
     *
     * @param objectiveId an Integer representing the id of the chosen objective.
     * @param success     a boolean value representing the success of the operation from the server.
     * @return a boolean value representing the success of the choice locally.
     */
    public boolean choosePersonalObjectiveClient(int objectiveId, Boolean success) {
        if (success) {
            getPlayerData().setPersonalObjective(Game.getObjectiveByID(objectiveId));
        }
        return success;
    }

    /**
     * Method used for choosing the player color.
     * It receives the chosen color from the user interface and the success of the operation from the server.
     *
     * @param playerColor the color chosen by the player.
     * @param success     a boolean value representing the success of the operation from the server.
     * @return a boolean value representing the success of the choice locally.
     */
    public boolean choosePlayerColorClient(Color playerColor, Boolean success) {
        if (success) {
            getPlayerData().setPlayerColor(playerColor);
        }
        return success;
    }

    /**
     * Method used for fetching available colors from the server.
     * It receives a list of available colors from the server and updates the player data accordingly.
     *
     * @param availableColors an ArrayList of Color objects representing available colors.
     * @return true if available colors were successfully fetched and updated locally, false otherwise.
     */
    public boolean fetchAvailableColorsClient(ArrayList<Color> availableColors) {
        if (availableColors == null) {
            return false;
        }

        getPlayerData().setAvailableColors(availableColors);
        return true;
    }

    /**
     * Method used for fetching the available games from the server.
     * If the available games are not null, it sets the available games of the client.
     *
     * @param availableGames an ArrayList of UUIDs representing the available games.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchAvailableGamesClient(Map<UUID, String> availableGames) {

        if (availableGames != null) {
            this.availableGames = availableGames;
            return !availableGames.isEmpty();
        }
        return false;
    }

    /**
     * Method used for fetching the game state from the server.
     * If the game state is not null, it sets the game state of the client.
     *
     * @param gameState a GameState representing the game state.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchGameStateClient(GameState gameState) {

        if (gameState == null) {
            return false;
        }
        this.gameState = gameState;
        return true;
    }

    /**
     * Method used for fetching the players from the server.
     * If the players are not null, it creates the player data for the players.
     *
     * @param players an ArrayList of Strings representing the players of the game.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchPlayersClient(ArrayList<String> players) {

        if (players == null) {
            return false;
        }

        createPlayerData(players);
        return true;
    }

    /**
     * Method used for fetching the common objectives from the server.
     * If the common objectives are not null, it sets the common objectives of the client.
     *
     * @param commonObjectivesId an ArrayList of Integers representing the ids of the common objectives.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchCommonObjectivesClient(ArrayList<Integer> commonObjectivesId) {

        if (commonObjectivesId == null) {
            return false;
        }

        ArrayList<Objective> commonObjectivesList = new ArrayList<>();

        for (Integer id : commonObjectivesId) {
            commonObjectivesList.add(Game.getObjectiveByID(id));
        }

        ((PlayerData) playerData.get(username)).setGlobalObjectives(commonObjectivesList);
        return true;
    }

    /**
     * Method used for fetching the personal objectives from the server.
     * If the personal objectives are not null, it sets the personal objective of the client.
     *
     * @param personalObjectiveId an Integer representing the id of the personal objective.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchPersonalObjectiveClient(Integer personalObjectiveId) {

        if (personalObjectiveId == null) {
            return false;
        }

        ((PlayerData) playerData.get(username)).setPersonalObjective(Game.getObjectiveByID(personalObjectiveId));
        return true;
    }

    /**
     * Method used for fetching the visible cards and the decks from the server.
     * Those are used to compose the draw pile of the game.
     * If the visible cards and the back side of the decks are not null, it sets the visible cards and the back side of the decks of the client.
     *
     * @param visibleCards  an ArrayList of Integers representing the ids of the visible cards.
     * @param backSideDecks an ArrayList of Integers representing the ids of the back side of the decks.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchVisibleCardsAndDecksClient(ArrayList<Integer> visibleCards, ArrayList<Integer> backSideDecks) {

        if (visibleCards == null || backSideDecks == null) {
            return false;
        }

        ArrayList<Card> visibleCardsList = new ArrayList<>();
        ArrayList<Card> backSideDecksList = new ArrayList<>();

        for (Integer id : visibleCards) {
            visibleCardsList.add(Game.getCardByID(id));
        }
        for (Integer id : backSideDecks) {
            backSideDecksList.add(Game.getCardByID(id));
        }

        this.visibleCards = visibleCardsList;
        this.backSideDecks = backSideDecksList;
        return true;
    }

    /**
     * Method used for fetching the score map from the server.
     * If the score map is not null, it sets the score map of the client.
     *
     * @param scoreMap a HashMap of Strings and Integers representing the score of each player.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchScoreMapClient(HashMap<String, Integer> scoreMap) {

        if (scoreMap == null) {
            return false;
        }
        for (String player : players) {
            if (!scoreMap.containsKey(player)) {
                scoreMap.put(player, 0);
            }
        }

        this.scoreMap = scoreMap;
        return true;
    }

    /**
     * Method used for fetching the player resources from the server.
     * It sets the resources of the players of the client.
     *
     * @param playersResourcesMap a HashMap of Strings and HashMaps of Resources and Integers representing the resources of each player.
     *                            The inner HashMap represents the resources of a player.
     *                            The String represents the player.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchPlayersResourcesClient(HashMap<String, HashMap<Resource, Integer>> playersResourcesMap) {

        for (String player : players) {
            playerData.get(player).setResources(playersResourcesMap.get(player));
        }

        return true;
    }

    /**
     * Method used for fetching the players boards from the server.
     * It sets the boards of the players of the client.
     *
     * @param playersBoardMap a HashMap of Strings and HashMaps of Coordinates and Integers representing the boards of each player.
     *                        The inner HashMap represents the board of a player.
     *                        The String represents the player.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchPlayersBoardsClient(HashMap<String, HashMap<Coordinates, Integer>> playersBoardMap) {
        for (String player : players) {
            BiMap<Coordinates, Card> playerBoard = HashBiMap.create();

            for (Coordinates c : playersBoardMap.get(player).keySet()) {
                playerBoard.put(c, Game.getCardByID(playersBoardMap.get(player).get(c)));
            }

            playerData.get(player).setBoard(playerBoard);
        }

        return true;
    }

    /**
     * Method used for fetching the players placing order from the server.
     * It sets the placing order of the players of the client.
     *
     * @param placingOrderMap a HashMap of Strings and ArrayLists of Cards representing the placing order of each player.
     *                        The ArrayList represents the placing order of a player.
     *                        The String represents the player.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchPlayersPlacingOrderClient(HashMap<String, ArrayList<Card>> placingOrderMap) {
        for (String player : players) {
            playerData.get(player).setOrder(placingOrderMap.get(player));
        }

        return true;
    }

    /**
     * Method used for fetching the main player possible card placements from the server.
     * If the valid placements are not null, it sets the valid placements of the client player.
     *
     * @param validPlacements an ArrayList of Coordinates representing the valid placements of the client player.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchValidPlacementsClient(ArrayList<Coordinates> validPlacements) {

        if (validPlacements == null) {
            return false;
        }

        ((PlayerData) playerData.get(username)).setValidPlacements(validPlacements);
        return true;
    }

    /**
     * Method used for fetching the main player's hand of cards from the server.
     * If the hand is not null, it sets the hand of the client player.
     *
     * @param handIds an ArrayList of Integers representing the ids of the cards in the hand of the client player.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchClientHandClient(ArrayList<Integer> handIds) {

        if (handIds == null || handIds.isEmpty()) {
            return false;
        }

        ArrayList<Card> hand = new ArrayList<>();

        for (int id : handIds) {
            hand.add(Game.getCardByID(id));
        }

        ((PlayerData) playerData.get(username)).setClientHand(hand);
        return true;
    }

    /**
     * Method used for fetching the main player's starting objectives from the server.
     * This method is used before the "chooseStartingObjective" method.
     * If the starting objectives are not null, it sets the starting objectives of the client player so that the user can choose one.
     *
     * @param startingObjectives an ArrayList of Integers representing the ids of the possible objectives of the client player.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchStartingObjectivesClient(ArrayList<Integer> startingObjectives) {

        if (startingObjectives == null) {
            return false;
        }

        ArrayList<Objective> startingObjectivesList = new ArrayList<>();
        for (int id : startingObjectives) {
            startingObjectivesList.add(Game.getObjectiveByID(id));
        }

        getPlayerData().setStartingObjectives(startingObjectivesList);
        return true;
    }

    /**
     * Method used for fetching the main player's starting card from the server.
     * This method is used before the "placeStartingCard" method.
     * If the starting card is not null, it sets the starting card of the client player so that the user can place it.
     *
     * @param startingCardId an Integer representing the id of the starting card of the client player.
     * @return a boolean value representing the success of the set operation.
     */
    public boolean fetchStartingCardClient(Integer startingCardId) {

        if (startingCardId == null) {
            return false;
        }

        ((PlayerData) playerData.get(username)).setStartingCard((StartingCard) Game.getCardByID(startingCardId));
        return true;
    }
}
