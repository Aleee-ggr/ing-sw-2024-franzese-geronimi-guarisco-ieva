package it.polimi.ingsw.network.messages.responses;

/**
 * This class represents a response message containing the player's personal objective.
 * It extends the GenericResponseMessage class.
 */
public class FetchPersonalObjectiveResponseMessage extends GenericResponseMessage {
    private final Integer personalObjective;

    /**
     * Constructs a new FetchPersonalObjectiveResponseMessage with the specified personal objective.
     *
     * @param personalObjective the player's personal objective.
     */
    public FetchPersonalObjectiveResponseMessage(Integer personalObjective) {
        this.personalObjective = personalObjective;
    }

    /**
     * Returns the player's personal objective.
     *
     * @return the player's personal objective.
     */
    public Integer getPersonalObjective() {
        return personalObjective;
    }

}
