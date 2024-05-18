package it.polimi.ingsw.network.messages.responses;

import java.util.HashMap;

public class GetScoreMapResponseMessage extends GenericResponseMessage{
    private final HashMap<String, Integer> scoreMap;

    public GetScoreMapResponseMessage(HashMap<String, Integer> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public HashMap<String, Integer> getScoreMap() {
        return scoreMap;
    }
}