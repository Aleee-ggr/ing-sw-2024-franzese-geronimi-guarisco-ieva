package it.polimi.ingsw.model.player;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Class is called by Game Class.
 * Contains the Player username, score, hiddenObjective, resources, hand and board.
 * @author Alessio Guarisco
 * @see Game
 * @see it.polimi.ingsw.model.cards.Deck
 * @see Card
 * @see Resource
 * @see Objective
 * */
public class Player {
    private final String username;
    private int score;
    private PlayerBoard board;
    private Card[] hand;
    private Objective hiddenObjective;
    private final ConcurrentHashMap<Resource, Integer> playerResources = new ConcurrentHashMap<>();
    private final Game game;

    /**
     * Constructor of Player
     * set up a new Player and sets all Resources to zero.
     * @param username it is the unique identifier of the player.
     * @param currentGame pointer to the instance of game the player is playing.
     * */
    public Player(String username, Game currentGame) {
        this.username = username;
        this.game = currentGame;
        this.score = 0;
        for (Resource r : Resource.values()){
            playerResources.put(r, 0);
        }
    }

    /**
     * Getter for the unique username of the Player.
     * @return a String username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the score of the Player.
     * @return a score as an int.
     */
    public int getScore(){
        return score;
    }

    /**
     * Getter for the PlayerBoard of the Player.
     * @return a PlayerBoard.
     * @see PlayerBoard
     */
    public PlayerBoard getPlayerBoard() {
        return board;
    }

    /**
     * Getter for the hand of Cards of the Player.
     * @return an array of Card.
     * @see Card
     */
    public Card[] getHand(){
        return hand;
    }

    /**
     * Getter for the Hidden Objective of the Player.
     * @return an Objective.
     * @see Objective
     */
    public Objective getHiddenObjective(){
        return hiddenObjective;
    }

    /**
     * Getter for the Resources of the Player.
     * @return a Map of Resources and the number of resources.
     * @see Resource
     */
    public Map<Resource, Integer> getResources() {
        return Collections.unmodifiableMap(playerResources);
    }

    /**
     * Setter for the score of the Player.
     * @param score is an int value of the score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Setter for the Hidden Object of the Player.
     * @param hiddenObjective is the Objective to set.
     * @see Objective
     */
    public void setHiddenObjective(Objective hiddenObjective) {
        this.hiddenObjective = hiddenObjective;
    }


    /**
     * Method used to set up the playerBoard, placing the first card in the Board.
     * It calls the constructor of PlayerBoard.
     * @see PlayerBoard
     * */
    public void setFirstCard(StartingCard card) {
        this.board = new PlayerBoard(card, this);
    }

    /**
     * Method used to update the number of resources of the Player.
     * @param r is the resource that needs to change value
     * @param value is the integer that represent the change in number of resources.
     * @see Resource
     * */
    public void updateResourcesValue(Resource r, int value){
        playerResources.put(r, playerResources.get(r) + value);
    }

    /**
     * Method to draw from one of the two decks in the SharedBoard of the Game <br/>
     * implements  {@link  #toHand(Card)  toHand} private method
     * @param isGold is a boolean used to identify if the card is drawn to the gold card deck or the std deck
     * */
    public void drawDecks(boolean isGold){
        toHand(game.getGameBoard().drawDeck(isGold));
    }

    /**
     * Method to draw from one of the four visible cards in the SharedBoard of the Game <br/>
     * implements  {@link  #toHand(Card)  toHand} private method
     * @param numVisible is used to choose the card from the board
     * @see it.polimi.ingsw.model.board.SharedBoard
     * */
    public void drawVisible(int numVisible){
        toHand(game.getGameBoard().drawVisible(numVisible));
    }

    /**
     * This method is used while the game is starting to draw the first hand.
     * It takes const values from:
     * @see it.polimi.ingsw.GameConsts
     * */
    public void drawFirstHand(){
        this.hand = new Card[GameConsts.firstHandDim];
        for (int i = 0; i < GameConsts.fistHandStdNum; i++){
            this.hand[i] = game.getGameBoard().drawDeck(false);
        }
        for (int i = GameConsts.fistHandStdNum; i < GameConsts.firstHandDim; i++){
            this.hand[i] = game.getGameBoard().drawDeck(true);
        }
    }

    /**
     * Private general method to add a drawn card to the hand of the player <br/>
     * used in {@link  #drawVisible(int) drawVisible} and
     * {@link  #drawDecks(boolean) drawVisible}
     * @param drawnCard is a Card obj from the Card class
     * @see Card
     * */
    private void toHand(Card drawnCard){
        for(int i = 0; i < GameConsts.firstHandDim; i++){
            if (hand[i]==null){
                hand[i]=drawnCard;
                return;
            }
        }
        System.out.println("too many cards");
    }

    /**
     * TODO: methods to implement
     */
    public void playCard(Card playedCard){

    }
}
