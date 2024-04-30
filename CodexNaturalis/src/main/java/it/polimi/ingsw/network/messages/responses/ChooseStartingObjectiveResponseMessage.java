package it.polimi.ingsw.network.messages.responses;

public class ChooseStartingObjectiveResponseMessage extends GenericResponseMessage{
    private final boolean correct;

    public ChooseStartingObjectiveResponseMessage(boolean correct) {
        this.correct = correct;
    }

    public boolean isCorrect() {
        return correct;
    }
}
