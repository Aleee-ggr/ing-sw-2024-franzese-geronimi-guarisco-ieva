package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.TUI.Compositor;
import it.polimi.ingsw.view.TUI.components.StartingCardView;
import it.polimi.ingsw.view.TUI.components.StartingObjectiveView;
import it.polimi.ingsw.view.TUI.components.printables.ObjectiveCard;

import java.io.PrintWriter;
import java.util.Scanner;

class ViewHandler extends Thread {
    private final Shared<ViewState> state;
    private final Shared<String> input;
    private final String username;
    private final Scanner in = new Scanner(System.in);
    private final PrintWriter out = new PrintWriter(System.out, true);
    private String[] players;

    public ViewHandler(Shared<ViewState> state, Shared<String> input, String username) {
        this.state = state;
        this.input = input;
        this.username = username;
    }

    @Override
    public void run() {
        Compositor compositor = null;

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
                    out.print("\033[H\033[2J");
                    out.flush();
                    ObjectiveCard[] objectives = Client.getData()
                            .getStartingObjectives()
                            .stream()
                            .map((Game::getObjectiveByID))
                            .map(ObjectiveCard::new)
                            .toArray(ObjectiveCard[]::new);
                    StartingObjectiveView startingObjectiveView = new StartingObjectiveView(objectives);
                    out.println(startingObjectiveView);
                    break;

                case WAIT_TURN:
                    sleepAndClear();
                    System.out.println("Waiting for turn...");
                    break;

                case PLACE_CARD, DRAW:
                    out.print("\033[H\033[2J");
                    out.flush();
                    if (compositor == null) {
                        compositor = new Compositor(players);
                    }
                    out.print(compositor);
                    input.setElement(in.nextLine());
                    break;

                default:
                    throw new RuntimeException("Unknown state %s".formatted(state.getElement()));
            }
        }
    }

    private  void sleepAndClear() {try {
        sleep(50); //20Hz update
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
        out.print("\033[H\033[2J");
        out.flush();

    }
}
