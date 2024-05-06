package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;

import java.util.UUID;


public class TuiController {
    private final Shared<ViewState> state = new Shared<>(ViewState.LOBBY);
    private final Shared<String> input = new Shared<>(null);

    public TuiController(RmiClient client, UUID game) {
        new RmiRemoteHandler(client, state, input, game).start();
        new ViewHandler(state, input, Client.getData().getUsername()).start();
    }

    public TuiController(SocketClient client) {}
}
