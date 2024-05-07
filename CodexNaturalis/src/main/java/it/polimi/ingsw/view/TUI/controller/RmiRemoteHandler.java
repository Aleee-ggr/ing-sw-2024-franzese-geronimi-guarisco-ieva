package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.rmi.RmiClient;

import java.rmi.RemoteException;
import java.util.UUID;

class RmiRemoteHandler extends Thread {
    private final RmiClient client;
    private final Shared<ViewState> gameState;
    private final Shared<String> input;
    private String[] players;
    private UUID game;

    RmiRemoteHandler(RmiClient client, Shared<ViewState> gameState, Shared<String> input, UUID game) {
        this.client = client;
        this.game = game;
        this.gameState = gameState;
        this.input = input;
    }


    @Override
    public void run() {
        joinGame();
        gameState.setElement(ViewState.LOBBY);
        while (gameState.getElement() != ViewState.STOP) {
            try {
                switch (gameState.getElement()) {
                    case LOBBY:
                        client.waitUpdate();
                        gameState.setElement(ViewState.SETUP_FETCH);
                        break;

                    case SETUP_FETCH:
                        setupFetch();
                        gameState.setElement(ViewState.SETUP_STARTING);
                        break;

                    case SETUP_STARTING:
                        waitInput();
                        setupStarting();
                        input.setElement(null);
                        gameState.setElement(ViewState.SETUP_OBJECTIVES);
                        break;

                    case SETUP_OBJECTIVES:
                        waitInput();
                        setupObjectives();
                        input.setElement(null);
                        gameState.setElement(ViewState.FETCH_DATA);
                        break;

                    case FETCH_DATA:
                        client.waitUpdate();
                        fetch();
                        gameState.setElement(ViewState.PLACE_CARD);
                        break;

                    case PLACE_CARD:
                        waitInput();
                        gameState.setElement(ViewState.DRAW);
                        input.setElement(null);
                        break;

                    case DRAW:
                        waitInput();
                        gameState.setElement(ViewState.FETCH_DATA);
                        input.setElement(null);
                        break;
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void waitInput() {
        while (input.getElement() == null) {
            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void joinGame() {
        try {
            if (game == null) {
                game = client.newGame(2);
            } else {
                client.joinGame(game);
            }
        } catch (ServerConnectionException | RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println(game);
    }

    private void setupFetch() {
        try {
            client.getStartingCard();
            client.getPlayers();
            client.getStartingObjectives();
            gameState.setElement(ViewState.SETUP_STARTING);
        } catch (ServerConnectionException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupStarting() {
        Integer side = Integer.valueOf(input.getElement());
        try {
            client.setStartingCard(side == 1);
        } catch (ServerConnectionException|RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupObjectives() {
        int in = Integer.parseInt(input.getElement());
        try {
            client.chooseStartingObjective(in);
        } catch (ServerConnectionException|RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetch() {
        try {
            client.getHand();
            client.getCommonObjectives();
            client.getPlayerBoard();
            client.getSharedBoard();
            client.getVisibleCards();
            client.getBackSideDecks();
            client.getPlayerResources(Client.getData().getUsername());
            //TODO add other getters
        } catch (ServerConnectionException|RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
