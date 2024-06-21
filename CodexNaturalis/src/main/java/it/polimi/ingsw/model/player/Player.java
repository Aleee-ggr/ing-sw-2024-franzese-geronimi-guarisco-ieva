package it.polimi.ingsw.model.player;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.RequirementsError;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Class is called by Game Class.
 * Contains the Player username, score, hiddenObjective, resources, hand and board.
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
    private final ColoredCard[] hand = new ColoredCard[GameConsts.firstHandDim];
    private Objective hiddenObjective;
    private Objective[] startingObjectives;
    protected final ConcurrentHashMap<Resource, Integer> playerResources = new ConcurrentHashMap<>();
    private StartingCard startingCard;
    private final Game game;

    private Color playerColor;

    /**
     * Constructor of Player <br/>
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

    public Color getPlayerColor() {
        return playerColor;
    }

    public ArrayList<Color> getAvailableColors(){
        return game.getAvailableColors();
    }

    /**
     * Getter for the score of the Player.
     * @return a score as an int.
     */
    public int getScore(){
        return this.score;
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
    public ColoredCard[] getHand(){
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
     * Getter for the Starting Objectives of the Player.
     * @return an ArrayList of Objectives.
     * @see Objective
     */
    public ArrayList<Objective> getStartingObjectives() {
        if(startingObjectives == null){
            throw new RuntimeException("Starting Objectives not set");
        }
        return new ArrayList<Objective>(List.of(startingObjectives));
    }

    /**
     * Getter for the Resources of the Player.
     * @return a ConcurrentHashMap of Resources and the number of resources.
     * @see Resource
     */
    public ConcurrentHashMap<Resource, Integer> getResources() {
        return new ConcurrentHashMap<>(playerResources);
    }

    /**
     * Getter for the Starting Card of the Player.
     * @return a StartingCard.
     * @see StartingCard
     */
    public StartingCard getDrawnStartingCard() {
        return startingCard;
    }

    /**
     * Setter for the score of the Player.
     * @param score is an int value of the score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Draws the objectives to chose from at the start of the game.
     * @see Objective
     */
    public void setStartingObjectives() {
        startingObjectives = new Objective[GameConsts.objectivesToChooseFrom];
        for(int i = 0; i < GameConsts.objectivesToChooseFrom; i++){
            startingObjectives[i] = game.getGameObjDeck().draw();
        }
    }

    /**
     * Choose the Hidden Object of the Player.
     * @param objId int id of the chosen card
     * @see Objective
     */
    public void choosePersonalObjective(int objId) {
        for(Objective obj: startingObjectives){
            if(obj.getId() == objId){
                hiddenObjective = obj;
            }
        }
    }

    /**
     * Sets the player color to the specified color.
     *
     * @param playerColor The color to set for the player.
     */
    public void choosePlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }


    /**
     * Method used to set up the playerBoard, placing the first card in the Board.<br/>
     * It calls the constructor of PlayerBoard.
     * @param frontsideup boolean if the user wants the starting card to be front side up
     * @see PlayerBoard
     * */
    public void setFirstCard(boolean frontsideup) {
        startingCard.setFrontSideUp(frontsideup);
        this.board = new PlayerBoard(startingCard, this);
    }

    /**
     * Method used to draw the Starting Card of the Player.<br/>
     * It calls the draw method of the GameStartingDeck.
     * @return the StartingCard drawn
     * @see StartingCard
     * */
    public StartingCard drawStartingCard() {
        startingCard = game.getGameStartingDeck().draw();
        return startingCard;
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
     * implements  {@link  #toHand(ColoredCard)  toHand} private method
     * @param isGold is a boolean used to identify if the card is drawn to the gold card deck or the std deck
     * @return the ColoredCard drawn
     * */
    public ColoredCard drawDecks(boolean isGold){
        ColoredCard card = game.getGameBoard().drawDeck(isGold);
        toHand(card);
        return card;
    }

    /**
     * Method to draw from one of the four visible cards in the SharedBoard of the Game <br/>
     * implements  {@link  #toHand(ColoredCard)  toHand} private method
     * @param numVisible is used to choose the card from the board
     * @see it.polimi.ingsw.model.board.SharedBoard
     * @return the ColoredCard drawn
     * */
    public ColoredCard drawVisible(int numVisible){
        ColoredCard card = game.getGameBoard().drawVisible(numVisible);
        toHand(card);
        return card;
    }

    /**
     * This method is used while the game is starting to draw the first hand.<br/>
     * It takes const values from:
     * @see it.polimi.ingsw.GameConsts
     * */
    public void drawFirstHand(){
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
    private void toHand(ColoredCard drawnCard){
        for(int i = 0; i < GameConsts.firstHandDim; i++){
            if (hand[i]==null){
                hand[i]=drawnCard;
                return;
            }
        }
        System.out.println("too many cards");
    }

    /**
     * Plays a card on the game board at the specified coordinates.
     * If the played card is a GoldCard, and it meets the player's requirements,
     * it is placed on the board and the player's score is updated.
     * If the played card is a StdCard, it is placed on the board and if it is a point card,
     * the player's score is updated.
     * Finally, the played card is removed from the player's hand.
     *
     * @param playedCard   The card to be played.
     * @param coordinates  The coordinates where the card should be placed on the board.
     *
     */
    public void playCard(ColoredCard playedCard, Coordinates coordinates) throws RequirementsError {
        if (playedCard.isFrontSideUp()) {
            if (playedCard instanceof GoldCard goldCard) {
                if (goldCard.checkRequirements(this)) {
                    board.placeCard(goldCard, coordinates);
                    game.getGameBoard().updateScore(this, goldCard.getScore(this));
                    this.score = game.getGameBoard().getScore().get(this);
                }
                else {
                    throw new RequirementsError();
                }
            } else {
                StdCard stdCard = (StdCard) playedCard;
                board.placeCard(stdCard, coordinates);
                if (stdCard.isPoint()) {
                    game.getGameBoard().updateScore(this, 1);
                    this.score = game.getGameBoard().getScore().get(this);
                }
            }
        } else {
            board.placeCard(playedCard, coordinates);
        }

        for (int i = 0; i < GameConsts.firstHandDim; i++) {
            if (hand[i].equals(playedCard)) {
                hand[i] = null;
            }
        }
    }
}
