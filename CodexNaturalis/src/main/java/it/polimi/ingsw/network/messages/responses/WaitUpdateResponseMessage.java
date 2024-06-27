package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.controller.WaitState;

/**
 * This class represents a response message containing an update about the wait state.
 * It extends the GenericResponseMessage class.
 */
public class WaitUpdateResponseMessage extends GenericResponseMessage {
    private final WaitState waitState;

    /**
     * Constructs a new WaitUpdateResponseMessage with the specified wait state.
     *
     * @param waitState the current wait state.
     */
    public WaitUpdateResponseMessage(WaitState waitState) {
        this.waitState = waitState;
    }

    /**
     * Returns the current wait state.
     *
     * @return the current wait state.
     */
    public WaitState getWaitState() {
        return waitState;
    }
}