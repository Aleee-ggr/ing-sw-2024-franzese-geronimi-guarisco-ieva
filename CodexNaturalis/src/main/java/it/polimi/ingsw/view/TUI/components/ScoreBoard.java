package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.ClientInterface;

import java.util.ArrayList;

/**
 * Represents the scoreboard component in the TUI for the game.
 * This component displays the scores of all players in a formatted manner.
 */
public class ScoreBoard implements Component {
    public static final int scoreHeight = 5;
    public static final int scoreWidth = 19;
    private final ArrayList<String> scoreBoard = new ArrayList<>(scoreHeight);
    private final ClientInterface client;
    private final ArrayList<String> players;

    /**
     * Constructs a ScoreBoard object initialized with a client interface.
     *
     * @param client The client interface used to fetch player and score information.
     */
    public ScoreBoard(ClientInterface client) {
        this.client = client;
        this.players = client.getPlayers();
    }

    /**
     * Converts the ScoreBoard object to its string representation.
     * This representation includes the formatted layout of the scoreboard with player names,
     * their corresponding scores, and colors representing each player.
     *
     * @return The string representation of the ScoreBoard.
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("ScoreBoard:        \n");

        int y;
        for (y = 0; y < players.size(); y++) {
            String player = players.get(y);
            String score = client.getScoreMap().get(player).toString();
            int maxLength = scoreWidth - score.length() - 2;

            Color playerColor;
            if (client.getUsername().equals(player)) {
                playerColor = client.getPlayerData().getPlayerColor();
            } else {
                playerColor = ((OpponentData) client.getOpponentData().get(player)).getPlayerColor();
            }

            String colorCode = getColorCode(playerColor);

            if (player.length() > maxLength) {
                player = player.substring(0, maxLength);
            }

            String coloredPlayer = colorCode + player + "\u001b[0m";

            int spacesCount = scoreWidth - (player.length() + score.length() + 2);
            String spaces = " ".repeat(spacesCount);
            out.append(String.format("%s: %s%s\n", coloredPlayer, spaces, score));
        }

        while (y < scoreHeight) {
            y++;
            out.append(" ".repeat(scoreWidth))
                    .append("\n");
        }

        return out.toString();
    }

    /**
     * Retrieves the ANSI escape code for coloring based on the player's color.
     *
     * @param color The color of the player.
     * @return The ANSI escape code for the specified color.
     */
    private String getColorCode(Color color) {
        return switch (color) {
            case RED -> "\u001b[1;31m"; // Rosso
            case BLUE -> "\u001b[1;34m"; // Blu
            case GREEN -> "\u001b[1;32m"; // Verde
            case YELLOW -> "\u001b[1;33m"; // Giallo
            case null -> "\u001b[0m"; // Reset
        };
    }
}
