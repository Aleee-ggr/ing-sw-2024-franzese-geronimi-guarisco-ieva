package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.network.ClientInterface;

import java.util.ArrayList;

public class ScoreBoard implements Component{
    public static final int scoreHeight = 5;
    public static final int scoreWidth = 19;
    private final ArrayList<String> scoreBoard = new ArrayList<>(scoreHeight);
    private final ClientInterface client;

    public ScoreBoard(ClientInterface client) {
        this.client = client;
    }

    @Override
    public String toString() {
        StringBuilder sb = get();
        return sb.toString();
    }

    public StringBuilder get() {
        StringBuilder out = new StringBuilder();
        out.append("ScoreBoard:        \n");

        for(String player: client.getScoreMap().keySet()){
            String score = client.getScoreMap().get(player).toString();
            int maxLength = scoreWidth - score.length() - 2;

            if (player.length() > maxLength) {
                player = player.substring(0, maxLength);
            }

            int spacesCount = scoreWidth - (player.length() + score.length() + 2);
            String spaces = " ".repeat(spacesCount);

            out.append(String.format("%s: %s%s\n", player, spaces, score));
        }


        if(client.getPlayerNum()< GameConsts.maxPlayersNum){
            if(client.getPlayerNum() == 2){
                out.append("                    \n");
            }
            out.append("                    \n");
        }

        return out;
    }
}
