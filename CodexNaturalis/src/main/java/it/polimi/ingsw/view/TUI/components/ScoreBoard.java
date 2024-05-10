package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.network.ClientInterface;

import java.util.ArrayList;

public class ScoreBoard implements Component{
    public static final int scoreHeight = 5;
    public static final int scoreWidth = 19;
    private final ArrayList<String> scoreBoard = new ArrayList<>(scoreHeight);
    private final ClientInterface client;
    private final ArrayList<String> players;

    public ScoreBoard(ClientInterface client) {
        this.client = client;
        this.players = client.getPlayers();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("ScoreBoard:        \n");

        int y;
        for (y = 0; y < players.size(); y++) {
            String player = players.get(y);
            String score = client.getScoreMap().get(player).toString();
            int maxLength = scoreWidth - score.length() - 2;
            if (player.length() > maxLength) {
                player = player.substring(0, maxLength);
            }
            int spacesCount = scoreWidth - (player.length() + score.length() + 2);
            String spaces = " ".repeat(spacesCount);
            out.append(String.format("%s: %s%s\n", player, spaces, score));
        }

        while (y < scoreHeight) {
            y++;
            out.append(" ".repeat(scoreWidth))
                    .append("\n");
        }

        return out.toString();
    }
}
