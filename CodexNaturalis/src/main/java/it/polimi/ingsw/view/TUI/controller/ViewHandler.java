package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.TUI.components.StartingCardView;

import java.io.PrintWriter;
import java.util.Scanner;

class ViewHandler extends Thread {
    private final Shared<ViewState> state;
    private final Shared<String> input;
    private final String username;
    private final Scanner in = new Scanner(System.in);
    private final PrintWriter out = new PrintWriter(System.out, true);


    public ViewHandler(Shared<ViewState> state, Shared<String> input, String username) {
        this.state = state;
        this.input = input;
        this.username = username;
    }

    @Override
    public void run() {
        while (state.getElement() != ViewState.STOP) {
            sleepAndClear();
            switch (state.getElement()) {
                case LOBBY, SETUP_FETCH:
                    out.println("waiting...");
                    break;
                case SETUP_STARTING:
                    StartingCardView startingCardView = new StartingCardView(
                            (StartingCard) Game.getCardByID(Client.getData().getStartingCard(username))
                    );
                    out.println(startingCardView);
                    input.setElement(String.valueOf(in.nextInt()));
                    break;
                case SETUP_OBJECTIVES:
                    /* TODO starting objectives
                    Objective[] objectives =
                    StartingObjectiveView startingObjectiveView = new StartingObjectiveView(
                    );
                     */
                default:
                    throw new RuntimeException("Unknown state %s".formatted(state.getElement()));
            }
        }
    }

    private static void sleepAndClear() {try {
        sleep(50); //20Hz update
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
        System.out.print("\033[H\033[2J");
        System.out.flush();

    }
}
