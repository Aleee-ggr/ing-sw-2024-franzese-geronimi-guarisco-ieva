package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.UUID;

public interface ClientInterface {
    UUID newGame(int players);
    boolean joinGame(UUID game);
    boolean checkCredentials(String username, String password);
    void waitUpdate();
    void postChat(String message);
    void drawCard(int position);
    boolean placeCard(Coordinates coordinates, int CardId);
    boolean placeStartingCard(boolean frontSideUp);
    boolean chooseStartingObjective(int objectiveId);

    boolean fetchAvailableGames();
    boolean fetchGameState();
    boolean fetchPlayers();
    boolean fetchCommonObjectives();
    boolean fetchVisibleCardsAndDecks();
    boolean fetchScoreMap();
    boolean fetchPlayersResources();
    boolean fetchPlayersBoards();
    boolean fetchValidPlacements();
    boolean fetchClientHand();
    boolean fetchOpponentsHandColor();
    boolean fetchStartingObjectives();
    boolean fetchStartingCard();
}
