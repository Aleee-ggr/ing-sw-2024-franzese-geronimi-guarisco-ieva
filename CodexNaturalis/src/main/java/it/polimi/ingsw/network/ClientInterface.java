package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.ClientData;
import it.polimi.ingsw.model.client.PlayerData;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface ClientInterface {
    UUID newGame(int players) throws IOException;
    boolean joinGame(UUID game) throws IOException;
    boolean checkCredentials(String username, String password) throws IOException;
    void waitUpdate() throws IOException;
    void postChat(String message) throws IOException;
    void drawCard(int position) throws IOException;
    boolean placeCard(Coordinates coordinates, int CardId) throws IOException;
    boolean placeStartingCard(boolean frontSideUp) throws IOException;
    boolean choosePersonalObjective(int objectiveId) throws IOException;

    boolean fetchAvailableGames() throws IOException;
    boolean fetchGameState() throws IOException;
    boolean fetchPlayers() throws IOException;
    boolean fetchCommonObjectives() throws IOException;
    boolean fetchVisibleCardsAndDecks() throws IOException;
    boolean fetchScoreMap() throws IOException;
    boolean fetchPlayersResources() throws IOException;
    boolean fetchPlayersBoards() throws IOException;
    boolean fetchPlayersPlacingOrder() throws RemoteException;
    boolean fetchValidPlacements() throws IOException;
    boolean fetchClientHand() throws IOException;
    boolean fetchOpponentsHandColor() throws IOException;
    boolean fetchStartingObjectives() throws IOException;
    boolean fetchStartingCard() throws IOException;

    ArrayList<Card> getVisibleCards();
    ArrayList<Card> getDecksBacks();

    GameState getGameState();
    int getPlayerNum();
    HashMap<String, ClientData> getOpponentData();
    PlayerData getPlayerData();
    HashMap<String, Integer> getScoreMap();
    ArrayList<UUID> getAvailableGames();
    ArrayList<String> getPlayers();
    String getUsername();
    void setCredentials(String username, String password);
}
