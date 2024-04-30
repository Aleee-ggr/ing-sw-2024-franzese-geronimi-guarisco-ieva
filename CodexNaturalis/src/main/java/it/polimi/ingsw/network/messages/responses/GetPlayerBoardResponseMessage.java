package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.HashMap;

public class GetPlayerBoardResponseMessage extends GenericResponseMessage{
    private final HashMap<Coordinates, Integer> playerBoard;
    private final String usernameRequiredData;

    public GetPlayerBoardResponseMessage(HashMap<Coordinates, Integer> playerBoard, String usernameRequiredData){
        this.playerBoard = playerBoard;
        this.usernameRequiredData = usernameRequiredData;
    }

    public HashMap<Coordinates, Integer> getPlayerBoard(){
        return playerBoard;
    }

    public String getUsernameRequiredData(){
        return usernameRequiredData;
    }

}
