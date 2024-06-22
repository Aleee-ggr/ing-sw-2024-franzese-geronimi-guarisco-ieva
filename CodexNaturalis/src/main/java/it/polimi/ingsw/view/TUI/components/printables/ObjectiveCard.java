package it.polimi.ingsw.view.TUI.components.printables;

import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.view.TUI.components.Component;

import java.util.Map;

/**
 * Represents an objective card in a board game, formatted for display in the TUI.
 * Supports two types of objectives: pattern-based and resource-based.
 */
public class ObjectiveCard implements Component {
    public static final int width = 15;
    public static final int height = 5;
    public final int id;

    private static final String resourceObjective = """
            ┏━━━━━━━━━━━━━┓
            ┃     %c %c     ┃
            ┃     %c %c     ┃
            ┃     %c %c     ┃
            ┗━━━━━━━━━━━━━┛""";

    private static final String patternObjective = """
            ┏━━━━━━━━━━━━━┓
            ┃     %c%c%c     ┃
            ┃     %c%c%c     ┃
            ┃     %c%c%c     ┃
            ┗━━━━━━━━━━━━━┛""";

    private final Objective objective;

    private final String card;

    /**
     * Constructs an ObjectiveCard with the specified objective.
     *
     * @param objective The objective to display on the card.
     */
    public ObjectiveCard(Objective objective) {
        this.objective = objective;
        card = setCard(objective);
        this.id = objective.getId();
    }

    @Override
    public String toString()  {
        return card;
    }

    /**
     * Sets the card representation based on the type of objective.
     *
     * @param objective The objective to format.
     * @return The formatted string representation of the objective.
     * @throws IllegalStateException if the objective type is unexpected.
     */
    private static String setCard(Objective objective) {
        return switch (objective.getType()) {
            case "pattern" ->
                    formatPattern(objective);
            case "resources" ->
                    formatResource(objective);
            default -> throw new IllegalStateException("Unexpected value: " + objective.getType());
        };
    }

    /**
     * Formats a pattern-based objective into the card template.
     *
     * @param objective The pattern-based objective.
     * @return The formatted string representation of the pattern objective.
     */
    private static String formatPattern(Objective objective) {
        char[][] pattern = new char[3][3];
        Resource[][] res_pattern = objective.getPattern();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                pattern[x][y] = res_pattern[x][y].toChar();
            }
        }
        return patternObjective.formatted(
                pattern[0][0], pattern[0][1], pattern[0][2],
                pattern[1][0], pattern[1][1], pattern[1][2],
                pattern[2][0], pattern[2][1], pattern[2][2]
        );
    }

    /**
     * Formats a resource-based objective into the card template.
     *
     * @param objective The resource-based objective.
     * @return The formatted string representation of the resource objective.
     */
    private static String formatResource(Objective objective) {

        char[][] resources = new char[2][3];
        int y = 0;
        for (Map.Entry<Resource, Integer> entry : objective.getResource().entrySet()) {
            resources[0][y] = entry.getKey().toChar();
            resources[1][y] = entry.getValue().toString().toCharArray()[0];
            y++;
        }
        for (;y < 3; y++) {
            resources[0][y] = ' ';
            resources[1][y] = ' ';
        }
        return resourceObjective.formatted(
                resources[0][0], resources[1][0],
                resources[0][1], resources[1][1],
                resources[0][2], resources[1][2]
        );
    }
}
