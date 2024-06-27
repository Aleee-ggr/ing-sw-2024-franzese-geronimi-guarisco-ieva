package it.polimi.ingsw.network.messages.responses;

import java.util.HashMap;

/**
 * This class represents a response message containing a map of player scores.
 * It extends the GenericResponseMessage class.
 */
public class GetScoreMapResponseMessage extends GenericResponseMessage {
    private final HashMap<String, Integer> scoreMap;

    /**
     * Constructs a new GetScoreMapResponseMessage with the specified score map.
     *
     * @param scoreMap a map containing player names as keys and their corresponding scores as values.
     */
    public GetScoreMapResponseMessage(HashMap<String, Integer> scoreMap) {
        this.scoreMap = scoreMap;
    }

    /**
     * Returns the map containing player scores.
     *
     * @return a map containing player names as keys and their corresponding scores as values.
     */
    public HashMap<String, Integer> getScoreMap() {
        return scoreMap;
    }
}