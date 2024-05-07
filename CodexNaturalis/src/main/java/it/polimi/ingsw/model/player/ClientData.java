package it.polimi.ingsw.model.player;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.helpers.exceptions.model.ElementNotInHand;
import it.polimi.ingsw.helpers.exceptions.model.HandFullException;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Resource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//TODO: DOCUMENTATION

/**
 * Represents the data associated with a client in the game, including username, hand, player number, player boards, and scores.
 * The hand of the Client is a list of Integer value representing IDs of cards.
 * @author Alessio Guarisco
 */
public class ClientData {
    private String username;
    private String password;
    private UUID gameId;
    private Set<Coordinates> validPlacements;
    private ArrayList<Coordinates> validPlacementsArray = new ArrayList<>(); //TODO: merge
    private ArrayList<Integer> hand = new ArrayList<>(GameConsts.firstHandDim);

    private final Map<String, PlayersDataLight> playersData = new HashMap<>();
    private ArrayList<String> players;
    private int playerNum;

    private final Integer[] globalObjectives = new Integer[GameConsts.globalObjectives];
    private ArrayList<Integer> startingObjectives = new ArrayList<>();
    private Integer personalObjective;

    private HashMap<String, Integer> scoreMap;

    private ArrayList<Integer> visibleCards;
    private ArrayList<Integer> backSideDecks;

    private GameState gameState;

    /**
     * Constructs a new ClientData object with the specified username.
     * @param username The username of the client.
     */
    public ClientData(String username, String password) {
        this.username = username;
        this.password = password;
        playersData.put(username, new PlayersDataLight());
        validPlacements = new HashSet<>();
    }

    /**
     * Getter for the hand of the client.
     * @return An array of CardID representing the hand of the client.
     */
    public ArrayList<Integer> getHand() {
        return hand;
    }

    public List<Resource> getPlayerHandColor(String username) {
        return playersData.get(username).getHandColor();
    }

    /**
     * Getter for the username of the client.
     * @return The username of the client.
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
        this.playerNum = players.size();
        for(String player : players){
            playersData.put(player, new PlayersDataLight());
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Getter for the player number of the client.
     * @return The player number of the client.
     */
    public int getPlayerNum() {
        return playerNum;
    }

    public Integer[] getGlobalObjectives() {
        return globalObjectives;
    }

    public Integer getPersonalObjective() {
        return personalObjective;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public ArrayList<Integer> getVisibleCards() {
        return visibleCards;
    }

    public ArrayList<Integer> getBackSideDecks() {
        return backSideDecks;
    }

    public HashMap<String, Integer> getScoreMap() {
        return scoreMap;
    }

    public void setPersonalObjective(Integer personalObjective) {
        this.personalObjective = personalObjective;
    }
    public void setStartingObjectives(ArrayList<Integer> startingObjectives) {
        this.startingObjectives = startingObjectives;
    }
    public void setVisibleCards(ArrayList<Integer> visibleCards) {
        this.visibleCards = visibleCards;
    }

    public void setBackSideDecks(ArrayList<Integer> backSideDecks) {
        this.backSideDecks = backSideDecks;
    }

    public void setScoreMap(HashMap<String, Integer> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public void setGlobalObjectives(Integer obj1, Integer obj2) {
        this.globalObjectives[0] = obj1;
        this.globalObjectives[1] = obj2;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void addPlayer(String username){
        playersData.put(username, new PlayersDataLight());
    }

    public Map<Coordinates, Integer> getClientBoard() {
        return Collections.unmodifiableMap(playersData.get(this.username).getBoard());
    }

    public ArrayList<Integer> getStartingObjectives() {
        return startingObjectives;
    }

    public ArrayList<Integer> getClientPlacingOrder(){
        return new ArrayList<>(playersData.get(this.username).getOrder());
    }

    public Map<Coordinates, Integer> getPlayerBoard(String username) {
        return Collections.unmodifiableMap(playersData.get(username).getBoard());
    }

    public ArrayList<Integer> getPlayerPlacingOrder(String username){
        return new ArrayList<>(playersData.get(username).getOrder());
    }


    public ConcurrentHashMap<String, Integer> getScoreBoard() {
        ConcurrentHashMap<String, Integer> scoreboard = new ConcurrentHashMap<>();
        playersData.remove(null); //TODO redo
        for(String player : playersData.keySet()){
            scoreboard.put(player, playersData.get(player).getScore());
        }
        return scoreboard;
    }

    /**
     * Setter for the number of players in the game.
     * @param playerNum The player number to set.
     */
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    /**
     * Setter for a new hand of id of cards for the client.
     * @param newHand The new hand to set.
     */
    public void setClientHand(ArrayList<Integer> newHand) {
        this.hand = newHand;
    }

    public void setPlayerHandColor(String username, ArrayList<Resource> newHand){
        this.playersData.get(username).updateHand(newHand);
    }

    public void setStartingCard(String username, Integer startingCardId) {
        this.playersData.get(username).setStartingCard(startingCardId);
    }

    public Integer getStartingCard(String username){
        return this.playersData.get(username).getStartingCard();
    }

    public void updatePlayerResources(String username, Map<Resource, Integer> newMap){
        this.playersData.get(username).updateResources(newMap);
    }

    public Set<Coordinates> getValidPlacements() {
        return validPlacements;
    }

    public ArrayList<Coordinates> getValidPlacementsArray() {
        return validPlacementsArray;
    }

    /**
     * Setter for the possible placement of Cards.
     * @param validPlacements The new set of Coordinates.
     */
    public void setValidPlacements(Set<Coordinates> validPlacements) { //TODO: to merge
        this.validPlacements = validPlacements;
        this.validPlacementsArray.addAll(validPlacements);
    }

    public void addPlayerCard(String username, Coordinates coordinates, Integer CardId){
        this.playersData.get(username).addCard(coordinates, CardId);
    }

    public void addValidPlacements(Coordinates coordinate){
        this.validPlacements.add(coordinate);
    }

    public void removeValidPlacements(Coordinates coordinate){
        this.validPlacements.remove(coordinate);
    }


    public void setClientBoard(HashMap<Coordinates, Integer> playerBoard) {
        playersData.get(this.username).setBoard(playerBoard);
    }

    public void setPlayerBoard(String username, HashMap<Coordinates, Integer> playerBoard) {
        playersData.get(username).setBoard(playerBoard);
    }


    /**
     * Updates the score of the client associated with the specified username.
     * @param username The username of the client.
     * @param newScore The new score to set.
     */
    public void updateScore(String username, Integer newScore) {
        playersData.get(username).setScore(newScore);
    }

    /**
     * Adds a card to the client's hand if the hand is not already full.
     * @param cardId The ID of the card to be added to the hand.
     * @throws HandFullException If the client's hand is already full.
     */
    public void addToHand(int cardId) throws HandFullException {
        if(hand.size() > GameConsts.firstHandDim){
            throw new HandFullException("client hand full");
        } else {
            hand.add(cardId);
        }
    }

    /**
     * Removes a card from the client's hand if it exists in the hand.
     * @param cardId The ID of the card to be removed from the hand.
     * @throws ElementNotInHand If the specified card is not in the client's hand.
     */
    public void removeFromHand(Integer cardId) throws ElementNotInHand { //TODO: test
        if(!hand.contains(cardId)){
            throw new ElementNotInHand("the client does not have this card!");
        } else {
            hand.remove(cardId);
        }
    }

}
