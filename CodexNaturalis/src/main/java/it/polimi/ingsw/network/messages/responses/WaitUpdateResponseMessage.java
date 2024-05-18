package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.controller.WaitState;

public class WaitUpdateResponseMessage extends GenericResponseMessage{
    private final WaitState waitState;

    public WaitUpdateResponseMessage(WaitState waitState) {
        this.waitState = waitState;
    }


    public WaitState getWaitState() {
        return waitState;
    }
}