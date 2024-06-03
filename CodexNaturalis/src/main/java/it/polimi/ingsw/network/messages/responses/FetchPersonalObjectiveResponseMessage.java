package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

public class FetchPersonalObjectiveResponseMessage extends GenericResponseMessage{
    private final Integer personalObjective;

    public FetchPersonalObjectiveResponseMessage(Integer personalObjective) {
        this.personalObjective = personalObjective;
    }

    public Integer getPersonalObjective() {
        return personalObjective;
    }

}
