package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.helpers.exceptions.model.ExistingUsernameException;
import it.polimi.ingsw.helpers.exceptions.model.TooManyPlayersException;
import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.abs;

/**
 * Game Class for creating a game with id, players, numPlayers and SharedBoard.
 *
 * @see Player
 * @see SharedBoard
 */
public class Game {
    static final ConcurrentHashMap<Integer, Card> cardID = new ConcurrentHashMap<>() {{
        for (Card gold : FullDeck.getFullGoldDeck().getCards()) {
            put(gold.getId(), gold);
        }
        for (Card std : FullDeck.getFullStdDeck().getCards()) {
            put(std.getId(), std);
        }
        for (Card start : FullDeck.getFullStartingDeck().getCards()) {
            put(start.getId(), start);
        }
    }};
    static final ConcurrentHashMap<Integer, Objective> objectiveID = new ConcurrentHashMap<>() {{
        for (Objective objective : FullDeck.getFullObjDeck().getCards()) {
            put(objective.getId(), objective);
        }
    }};
    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private final int maxPlayers;
    private final ArrayList<Color> availableColors = new ArrayList<>();
    /*Game-Specific Decks: not static decks for the instance of Game*/ Deck<GoldCard> gameGoldDeck;
    Deck<StdCard> gameStdDeck;
    Deck<Objective> gameObjDeck;
    Deck<StartingCard> gameStartingDeck;
    private int numPlayers = 0;
    private SharedBoard gameBoard;
    private GameState gameState;

    /**
     * Constructor for the Game class.
     */
    public Game(int maxPlayers) {
        resetBoard();
        this.maxPlayers = maxPlayers;
        this.manageObjectives();
        Collections.addAll(availableColors, Color.values());
    }

    /**
     * Get the card from the corresponding id
     *
     * @param id the id of the card
     * @return the card corresponding to the id
     */
    public static Card getCardByID(Integer id) {
        Card card = cardID.get(abs(id));
        if (id < 0) {
            card.setFrontSideUp(false);
        }
        return card;
    }

    /**
     * Get the objective from the corresponding id
     *
     * @param id the id of the objective
     * @return the objective corresponding to the id
     */
    public static Objective getObjectiveByID(Integer id) {
        return objectiveID.get(id);
    }

    /**
     * Getter for numPLayers of Game.
     *
     * @return the number of players in the Game, as int
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Getter for the list of Players in Game.
     *
     * @return the List of Players in the Game
     */
    public List<Player> getPlayers() {
        return players;
    }

    public ArrayList<Color> getAvailableColors() {
        return availableColors;
    }

    /**
     * Getter for the GameBoard.
     *
     * @return the SharedBoard of the Game
     */
    public SharedBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Getter for the full object deck.
     *
     * @return the gameObjDeck, game specific object deck
     */
    public Deck<Objective> getGameObjDeck() {
        return gameObjDeck;
    }

    /**
     * Getter for the full starting card deck.
     *
     * @return the gameStartingDeck, game specific starting card deck
     */
    public Deck<StartingCard> getGameStartingDeck() {
        return gameStartingDeck;
    }

    /**
     * Getter for the max number of players in the game.
     *
     * @return the max number of players in the game
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Getter for the GameState of the Game.
     *
     * @return the GameState of the Game
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Setter for the GameState of the Game.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Method used to add a Player in Game
     *
     * @param playerUsername is the player username as a String
     * @throws TooManyPlayersException   while trying to add a player when the Game is full
     * @throws ExistingUsernameException while there is already a player with the same username in game
     *                                   the exceptions are managed by the caller.
     */
    public void addPlayer(String playerUsername) throws TooManyPlayersException, ExistingUsernameException {
        if (this.numPlayers >= maxPlayers) {
            for (Player p : players) {
                if (p.getUsername().equals(playerUsername)) {
                    return;
                }
            }
            throw new TooManyPlayersException("Too Many Players");
        }

        for (Player p : players) {
            if (p.getUsername().equals(playerUsername)) {
                throw new ExistingUsernameException("Existing Username");
            }
        }

        Player toAdd = new Player(playerUsername, this);
        players.add(toAdd);
        this.numPlayers += 1;
    }

    /**
     * Method used to randomize the order of the players.<br/>
     * Used to randomize the turn order of the players in the game.
     */
    public void randomizePlayers(){
        Collections.shuffle(players);
    }

    /**
     * Method used to reset the SharedBoard. <br/>
     * It creates a new board with new shuffled decks.
     */
    public void resetBoard() {
        try {
            gameGoldDeck = FullDeck.getFullGoldDeck().shuffle();
            gameStdDeck = FullDeck.getFullStdDeck().shuffle();
            gameObjDeck = FullDeck.getFullObjDeck().shuffle();
            gameStartingDeck = FullDeck.getFullStartingDeck().shuffle();

            gameBoard = new SharedBoard(gameGoldDeck, gameStdDeck);
        } catch (RuntimeException e) {
            System.out.println("error while resetting the SharedBoard");
        }
    }

    /**
     * Method used to manage the objectives of the game. <br/>
     * It shuffles the deck and draws the objectives to be placed in the SharedBoard.
     */
    public void manageObjectives() {
        gameObjDeck.shuffle();
        Objective[] objectiveToAdd = new Objective[GameConsts.globalObjectives];
        for (int i = 0; i < GameConsts.globalObjectives; i++) {
            objectiveToAdd[i] = gameObjDeck.draw();
        }
        gameBoard.setObjectives(objectiveToAdd);
    }

    public void updateAvailableColors(Color playerColor) {
        availableColors.remove(playerColor);
    }
}
