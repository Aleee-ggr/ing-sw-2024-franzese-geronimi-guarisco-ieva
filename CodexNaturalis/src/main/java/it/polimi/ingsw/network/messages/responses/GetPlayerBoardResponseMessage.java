package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.HashMap;

/**
 * Represents a response message containing the player board's information.
 * Extends GenericResponseMessage.
 */
public class GetPlayerBoardResponseMessage extends GenericResponseMessage{
    private final HashMap<Coordinates, Integer> playerBoard;
    private final String usernameRequiredData;

    /**
     * Constructs a GetPlayerBoardResponseMessage with the specified player board and required username data.
     *
     * @param playerBoard the player board mapping coordinates to values
     * @param usernameRequiredData the username of the required data
     */
    public GetPlayerBoardResponseMessage(HashMap<Coordinates, Integer> playerBoard, String usernameRequiredData){
        this.playerBoard = playerBoard;
        this.usernameRequiredData = usernameRequiredData;
    }

    /**
     * Retrieves the player board mapping coordinates to values.
     *
     * @return the player board mapping coordinates to values
     */
    public HashMap<Coordinates, Integer> getPlayerBoard(){
        return playerBoard;
    }

    /**
     * Retrieves the username of the required data.
     *
     * @return the required username data
     */
    public String getUsernameRequiredData(){
        return usernameRequiredData;
    }
}
