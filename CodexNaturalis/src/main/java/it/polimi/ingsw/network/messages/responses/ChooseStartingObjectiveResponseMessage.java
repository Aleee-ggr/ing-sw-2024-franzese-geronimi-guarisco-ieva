package it.polimi.ingsw.network.messages.responses;

/**
 * Represents a response message indicating the result of choosing a starting objective.
 * Extends GenericResponseMessage.
 */
public class ChooseStartingObjectiveResponseMessage extends GenericResponseMessage{
    private final boolean correct;

    /**
     * Constructs a ChooseStartingObjectiveResponseMessage with the specified correctness status.
     *
     * @param correct true if the objective choice was correct, false otherwise
     */
    public ChooseStartingObjectiveResponseMessage(boolean correct) {
        this.correct = correct;
    }

    /**
     * Retrieves the correctness status of the objective choice.
     *
     * @return true if the objective choice was valid, false otherwise
     */
    public boolean isCorrect() {
        return correct;
    }
}
