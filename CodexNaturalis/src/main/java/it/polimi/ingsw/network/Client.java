package it.polimi.ingsw.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.helpers.exceptions.model.ElementNotInHand;
import it.polimi.ingsw.helpers.exceptions.model.HandFullException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.client.ClientData;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;

import javax.lang.model.type.ArrayType;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Abstract class, baseline for building a client that can connect to a server to participate in a game.
 * @author Alessio Guarisco
 * @author Daniele Ieva
 */
public class Client {
    protected final String serverAddress;
    protected final int serverPort;

    protected String username;
    protected String password;

    protected UUID gameId;
    protected GameState gameState;
    protected ArrayList<UUID> availableGames;
    protected int playerNum = 0;
    protected ArrayList<String> players;
    protected HashMap<String, ClientData> playerData;
    protected HashMap<String, Integer> scoreMap;

    protected ArrayList<Card> visibleCards;
    protected ArrayList<Card> backSideDecks;
    protected ArrayList<String> chat;


    /**
     * Constructs a new Client object with the specified player username, server address, and server port.
     * @param serverAddress  The address of the server.
     * @param serverPort     The port of the server.
     */
    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.availableGames = new ArrayList<>();
        this.players = new ArrayList<>();
        this.scoreMap = new HashMap<>();
        this.playerData = new HashMap<>();
        this.gameState = GameState.LOBBY;
    }

    public String getUsername() {
        return username;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ArrayList<UUID> getAvailableGames() {
        return this.availableGames;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public ArrayList<String> getPlayers() {
        return this.players;
    }

    public HashMap<String, ClientData> getOpponentData() {
        return this.playerData;
    }

    public HashMap<String, Integer> getScoreMap() {
        return this.scoreMap;
    }

    public ArrayList<Card> getVisibleCards() {
        return visibleCards;
    }

    public ArrayList<Card> getDecksBacks() {
        return backSideDecks;
    }

    public PlayerData getPlayerData() {
        return (PlayerData) playerData.get(username);
    }


    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public void createPlayerData(ArrayList<String> players) {
        for(String player : players){
            if(player.equals(this.username)) {
                playerData.put(player, new PlayerData());
            } else {
                playerData.put(player, new OpponentData());
            }
            this.scoreMap.put(player, 0);
        }

        this.players = players;
        this.playerNum = players.size();
    }


    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Username: " + this.username + "\n");
        for (String p : players) {
            out.append(p + ":\n");
            out.append(playerData.get(p).toString());
        }
        return out.toString();
    }






    //TODO: Implement for chat
    public void postChat(String message) throws IOException {

    }

    public boolean drawCardClient(Integer id){
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


    public boolean placeCardClient(Boolean success, int cardId) {
        if(success){
            try {
                ((PlayerData) playerData.get(username)).removeFromHand(Game.getCardByID(cardId));
            } catch (ElementNotInHand e) {
                throw new RuntimeException(e);
            }

            return true;
        }
        return false;
    }


    public boolean placeStartingCardClient(Boolean frontSideUp, Boolean success) {
        if(success){
            getPlayerData().setStartingCard((StartingCard) getPlayerData().getStartingCard().setFrontSideUp(frontSideUp));
        }
        return success;
    }

    public boolean choosePersonalObjectiveClient(int objectiveId, Boolean success) {
        if (success) {
            getPlayerData().setPersonalObjective(Game.getObjectiveByID(objectiveId));
        }
        return success;
    }

    public boolean fetchAvailableGamesClient(ArrayList<UUID> availableGames) {

        if(availableGames != null) {
            this.availableGames = availableGames;
            return !availableGames.isEmpty();
        }
        return false;
    }

    public boolean fetchGameStateClient(GameState gameState) {

        if (gameState == null){
            return false;
        }
        this.gameState = gameState;
        return true;
    }

    public boolean fetchPlayersClient(ArrayList<String> players) {

        if (players == null){
            return false;
        }

        createPlayerData(players);
        return true;
    }

    public boolean fetchCommonObjectivesClient(ArrayList<Integer> commonObjectivesId) {

        if(commonObjectivesId == null) {
            return false;
        }

        ArrayList<Objective> commonObjectivesList = new ArrayList<>();

        for (Integer id : commonObjectivesId) {
            commonObjectivesList.add(Game.getObjectiveByID(id));
        }

        ((PlayerData) playerData.get(username)).setGlobalObjectives(commonObjectivesList);
        return true;
    }

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

    public boolean fetchScoreMapClient(HashMap<String, Integer> scoreMap){

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

    public boolean fetchPlayersResourcesClient(HashMap<String, HashMap<Resource, Integer>> playersResourcesMap) {

        for(String player : players){
            playerData.get(player).setResources(playersResourcesMap.get(player));
        }

        return true;
    }

    public boolean fetchPlayersBoardsClient(HashMap<String, HashMap<Coordinates, Integer>> playersBoardMap) {
        for(String player : players){
            BiMap<Coordinates, Card> playerBoard = HashBiMap.create();

            for(Coordinates c : playersBoardMap.get(player).keySet()){
                playerBoard.put(c, Game.getCardByID(playersBoardMap.get(player).get(c)));
            }

            playerData.get(player).setBoard(playerBoard);
        }

        return true;
    }


    public boolean fetchPlayersPlacingOrderClient(HashMap<String, ArrayList<Card>> placingOrderMap) {
        for(String player : players){
            playerData.get(player).setOrder(placingOrderMap.get(player));
        }

        return true;
    }

    public boolean fetchValidPlacementsClient(ArrayList<Coordinates> validPlacements) {

        if (validPlacements == null) {
            return false;
        }

        ((PlayerData) playerData.get(username)).setValidPlacements(validPlacements);
        return true;
    }

    public boolean fetchClientHandClient(ArrayList<Integer> handIds) {

        if(handIds == null || handIds.isEmpty()){
            return false;
        }

        ArrayList<Card> hand = new ArrayList<>();

        for(int id : handIds){
            hand.add(Game.getCardByID(id));
        }

        ((PlayerData) playerData.get(username)).setClientHand(hand);
        return true;
    }

    public boolean fetchStartingObjectivesClient(ArrayList<Integer> startingObjectives) {

        if(startingObjectives == null){
            return false;
        }

        ArrayList<Objective> startingObjectivesList = new ArrayList<>();
        for(int id : startingObjectives){
            startingObjectivesList.add(Game.getObjectiveByID(id));
        }

        getPlayerData().setStartingObjectives(startingObjectivesList);
        return true;
    }

    public boolean fetchStartingCardClient(Integer startingCardId) {

        if(startingCardId == null){
            return false;
        }

        ((PlayerData)playerData.get(username)).setStartingCard((StartingCard) Game.getCardByID(startingCardId));
        return true;
    }

    public ArrayList<String> getChat() {
        return this.chat;
    }
}
