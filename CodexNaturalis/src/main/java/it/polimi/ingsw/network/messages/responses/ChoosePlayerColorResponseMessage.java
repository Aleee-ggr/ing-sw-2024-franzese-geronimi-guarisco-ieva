package it.polimi.ingsw.network.messages.responses;

public class ChoosePlayerColorResponseMessage extends GenericResponseMessage{
    private final boolean correct;

    public ChoosePlayerColorResponseMessage(boolean correct) {
        this.correct = correct;
    }

    public boolean isCorrect() {
        return correct;
    }
}