package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.view.TUI.components.printables.ObjectiveCard;

public class StartingObjectiveView implements Component{
    public static final int width = 166;
    public static final int height = 39;

    private final static String HEADER="STARTING OBJECTIVES";


    private final ObjectiveCard[] objectives;

    private final int cardSpacing = 5;
    private final int cardHeight;
    private final int cardWidth;

    private final int contentWidth;
    private final int marginSide;

    private final int contentHeight;
    private final int marginTop;

    private final int[] objectiveIds;

    public StartingObjectiveView(ObjectiveCard[] objectives) {
        this.objectives = objectives;
        objectiveIds = new int[]{
                objectives[0].id,
                objectives[1].id,
        };
        cardHeight = ObjectiveCard.height;
        cardWidth = ObjectiveCard.width;

        contentWidth = ((GameConsts.objectivesToChooseFrom) * cardWidth) + (cardSpacing) + 4;
        marginSide = (width - contentWidth) / 2;

        contentHeight = 5 + cardHeight;
        marginTop = (height - contentHeight) / 2;
    }

    public int[] getObjectives() {
        return objectiveIds;
    }

    @Override
    public String toString() {
        int header_padding = (contentWidth - HEADER.length()) / 2;
        StringBuilder out = new StringBuilder()
                .append(" ".repeat(marginSide))
                .append("╭")
                .append("─".repeat(contentWidth))
                .append("╮")
                .append("\n")
                .append(" ".repeat(marginSide))
                .append("│")
                .append(" ".repeat(header_padding))
                .append(HEADER)
                .append(" ".repeat(header_padding))
                .append("│")
                .append("\n")
                .append(" ".repeat(marginSide))
                .append("├")
                .append("─".repeat(contentWidth))
                .append("┤")
                .append("\n")
                .append(" ".repeat(marginSide))
                .append("│")
                .append("1.")
                .append(" ".repeat(((contentWidth - 4) / 2)+1))
                .append("2.")
                .append(" ".repeat((contentWidth - 4) / 2))
                .append("│")
                .append("\n");
        for (int i = 0; i < cardHeight; i++) {
            out.append(" ".repeat(marginSide))
                    .append("│")
                    .append(" ".repeat(cardSpacing/2))
                    .append(objectives[0].toStringArrayColor()[i])
                    .append(" ".repeat(cardSpacing))
                    .append(objectives[1].toStringArrayColor()[i])
                    .append(" ".repeat(cardSpacing/2))
                    .append("│\n");
        }
        out.append(" ".repeat(marginSide))
                .append("│")
                .append(" ".repeat(contentWidth))
                .append("│\n")
                .append(" ".repeat(marginSide))
                .append("╰")
                .append("─".repeat(contentWidth))
                .append("╯")
                .append("\n");

        return out.toString();
    }
}
