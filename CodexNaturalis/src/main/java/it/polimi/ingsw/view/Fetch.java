package it.polimi.ingsw.view;

import it.polimi.ingsw.network.ClientInterface;

import java.io.IOException;

import static java.lang.System.exit;

public abstract class Fetch {

    public static void fetchAllData(ClientInterface client) {
        try {
            client.fetchGameState();
            client.fetchPlayers();
            client.fetchPersonalObjective();
            client.fetchCommonObjectives();
            client.fetchClientHand();
            client.fetchPlayersBoards();
            client.fetchPlayersPlacingOrder();
            client.fetchValidPlacements();
            client.fetchPlayersResources();
            client.fetchScoreMap();
            client.fetchVisibleCardsAndDecks();
            client.fetchOpponentsHandColor();
            client.fetchOpponentsHandType();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit(1);
        }
    }

    public static void fetchAllDataGUI(ClientInterface client) {
        try {
            client.fetchGameState();
            client.fetchPersonalObjective();
            client.fetchCommonObjectives();
            client.fetchClientHand();
            client.fetchPlayersBoards();
            client.fetchPlayersPlacingOrder();
            client.fetchValidPlacements();
            client.fetchPlayersResources();
            client.fetchScoreMap();
            client.fetchVisibleCardsAndDecks();
            client.fetchOpponentsHandColor();
            client.fetchOpponentsHandType();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit(1);
        }
    }

    public static void fetchData(ClientInterface client) {
        try {
            client.fetchPlayersBoards();
            client.fetchScoreMap();
            client.fetchGameState();
            client.fetchVisibleCardsAndDecks();
            client.fetchChat();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit(1);
        }
    }

    public static void fetchPlace(ClientInterface client) {
        try {
            client.fetchClientHand();
            client.fetchValidPlacements();
            client.fetchPlayersBoards();
            client.fetchPlayersPlacingOrder();
            client.fetchPlayersResources();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit(1);
        }
    }

    public static void fetchDraw(ClientInterface client) {
        try {
            client.fetchClientHand();
            client.fetchVisibleCardsAndDecks();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit(1);
        }
    }

    public static void fetchStartTurn(ClientInterface client) {
        try {
            client.fetchValidPlacements();
            client.fetchPlayersPlacingOrder();
            client.fetchPlayersBoards();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit(1);
        }
    }

    public static void fetchSwitch(ClientInterface client) {
        try {
            client.fetchPlayersResources();
            client.fetchOpponentsHandColor();
            client.fetchOpponentsHandType();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit(1);
        }
    }

    /**
     * Fetches the initial setup data required for the game.
     */
    public static void fetchSetup(ClientInterface client) {
        try {
            client.fetchPlayers();
            client.fetchStartingObjectives();
            client.fetchStartingCard();
            client.fetchAvailableColors();
            client.fetchClientHand();
            client.fetchCommonObjectives();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit(1);
        }
    }
}
