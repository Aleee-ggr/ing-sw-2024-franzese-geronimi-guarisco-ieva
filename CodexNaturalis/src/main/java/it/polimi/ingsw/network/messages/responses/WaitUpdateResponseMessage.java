package it.polimi.ingsw.network.messages.responses;

public class WaitUpdateResponseMessage extends GenericResponseMessage{
    private final boolean isYourTurn;

    public WaitUpdateResponseMessage(boolean isYourTurn) {
        this.isYourTurn = isYourTurn;
    }

    public boolean isYourTurn() {
        return isYourTurn;
    }
}
