package it.polimi.ingsw.view.TUI.controller;

/*
class ViewHandler extends Thread {
    private final Shared<ViewState> state;
    private final Shared<String> input;
    private final String username;
    private final Scanner in = new Scanner(System.in);
    private final PrintWriter out = new PrintWriter(System.out, true);
    private String[] players;
    private final ClientInterface client;


    public ViewHandler(Shared<ViewState> state, Shared<String> input, String username, ClientInterface client) {
        this.state = state;
        this.input = input;
        this.username = username;
        this.client = client;
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
                    setupStarting();
                    break;

                case SETUP_OBJECTIVES:
                    setupObjectives();
                    break;

                case WAIT_TURN, FETCH_DATA:
                    sleepAndClear();
                    System.out.println("Waiting for turn...");
                    break;

                case PLACE_CARD, DRAW:
                    compositor = mainView(compositor);
                    break;

                default:
                    throw new RuntimeException("Unknown state %s".formatted(state.getElement()));
            }
        }
    }

    private Compositor mainView(Compositor compositor) {
        out.print("\033[H\033[2J");
        out.flush();
        if (compositor == null) {
            compositor = new Compositor(client.getPlayers().toArray(new String[0]), client);
        }
        out.print(compositor);
        input.setElement(in.nextLine());
        return compositor;
    }

    private void setupObjectives() {
        out.print("\033[H\033[2J");
        out.flush();
        ObjectiveCard[] objectives = ((PlayerData)client)
                .getStartingObjectives()
                .stream()
                .map(ObjectiveCard::new)
                .toArray(ObjectiveCard[]::new);
        StartingObjectiveView startingObjectiveView = new StartingObjectiveView(objectives);
        out.println(startingObjectiveView);
        int value = in.nextInt()-1;
        value = startingObjectiveView.getObjectives()[value];
        input.setElement(String.valueOf(value));
    }

    private void setupStarting() {
        StartingCardView startingCardView = new StartingCardView(
                ((PlayerData)client).getStartingCard()
        );
        out.println(startingCardView);
        int value = in.nextInt();
        if (value == 2) {
            value = -value;
        }
        input.setElement(String.valueOf(value));
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
*/