package it.polimi.ingsw.network.messages.responses;

/**
 * This class represents a response message indicating the result of choosing a player color.
 * It extends the GenericResponseMessage class.
 */
public class ChoosePlayerColorResponseMessage extends GenericResponseMessage {
    private final boolean correct;

    /**
     * Constructs a new ChoosePlayerColorResponseMessage with the specified correctness.
     *
     * @param correct a boolean indicating if the chosen player color is correct.
     */
    public ChoosePlayerColorResponseMessage(boolean correct) {
        this.correct = correct;
    }

    /**
     * Returns whether the chosen player color is correct.
     *
     * @return a boolean indicating if the chosen player color is correct.
     */
    public boolean isCorrect() {
        return correct;
    }
}