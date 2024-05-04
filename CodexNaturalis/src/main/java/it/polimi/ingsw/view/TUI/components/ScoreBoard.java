package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.player.ClientData;
import it.polimi.ingsw.network.Client;

import java.util.ArrayList;

public class ScoreBoard implements Component{
    public static final int scoreHeight = 5;
    public static final int scoreWidth = 19;
    private final ArrayList<String> scoreBoard = new ArrayList<>(scoreHeight);
    private final ClientData clientData = Client.getData();

    public String[] get() {

        scoreBoard.add("ScoreBoard:         ");

        for(String player: clientData.getScoreBoard().keySet()){
            String score = clientData.getScoreBoard().get(player).toString();
            int maxLength = scoreWidth - score.length() - 2;

            if (player.length() > maxLength) {
                player = player.substring(0, maxLength);
            }

            int spacesCount = scoreWidth - (player.length() + score.length() + 2);
            String spaces = " ".repeat(spacesCount);

            scoreBoard.add(String.format("%s: %s%s", player, spaces, score));
        }


        if(clientData.getPlayerNum()< GameConsts.maxPlayersNum){
            if(clientData.getPlayerNum() == 2){
                scoreBoard.add("                    ");
            }
            scoreBoard.add("                    ");
        }

        return scoreBoard.toArray(new String[0]);
    }
}
