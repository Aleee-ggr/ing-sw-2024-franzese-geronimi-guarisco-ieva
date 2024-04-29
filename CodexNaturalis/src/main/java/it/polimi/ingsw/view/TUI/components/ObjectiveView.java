package it.polimi.ingsw.view.TUI.components;


import it.polimi.ingsw.GameConsts;

public class ObjectiveView {
    private final ObjectiveCard personal;
    private final ObjectiveCard[] shared;

    private final int width = 146;
    private final int height = 27;

    private final int cardSpacing = 5;
    private final int cardHeight;
    private final int cardWidth;

    private final int contentWidth;
    private final int marginSide;

    private final int contentHeight;
    private final int marginTop;


    public ObjectiveView(ObjectiveCard personal, ObjectiveCard[] shared) {
        assert shared.length == GameConsts.globalObjectives;
        this.personal = personal;
        this.shared = shared;
        cardHeight = personal.toStringArray().length;
        cardWidth = personal.toStringArray()[0].length();

        contentWidth = ((1 + GameConsts.globalObjectives) * cardWidth) + (2 * cardSpacing) + 4;
        marginSide = (width - contentWidth) / 2;

        contentHeight = 5 + cardHeight;
        marginTop = (height - contentHeight) / 2;
    }

    public String[] toStringArray() {
        return toString().split("\n");
    }

    @Override
    public String toString() {
        StringBuilder out =  new StringBuilder();
        out.append("\n".repeat(marginTop));
        out.append(" ".repeat(marginSide))
                .append("╭")
                .append("─".repeat((cardWidth + cardSpacing/2) +1))
                .append("┬")
                .append("─".repeat((cardSpacing/2 + cardSpacing + 2 * cardWidth)+1))
                .append("╮")
                .append("\n");

        out.append(" ".repeat(marginSide))
                .append("│")
                .append("%s%s%s")
                .append("│")
                .append(" ".repeat(cardSpacing/2))
                .append("%s%s%s│\n")

                .append(" ".repeat(marginSide))
                .append("├")
                .append("─".repeat(cardWidth+1))
                .append("─".repeat(cardSpacing/2))
                .append("┼")
                .append("─".repeat(cardSpacing/2));
        for (int i = 0; i < GameConsts.globalObjectives - 1; i++) {
            out.append("─".repeat(cardWidth))
                    .append("─".repeat(cardSpacing));
        }
        out.append("─".repeat(cardWidth+1))
                .append("┤")
                .append("\n");

        for (int i = 0; i < cardHeight; i++) {
            out.append(" ".repeat(marginSide))
                    .append("│ ")
                    .append(personal.toStringArray()[i])
                    .append(" ".repeat(cardSpacing/2))
                    .append("│")
                    .append(" ".repeat(cardSpacing/2));
            for (int j = 0; j < GameConsts.globalObjectives - 1; j++) {
                out.append(shared[j].toStringArray()[i])
                        .append(" ".repeat(cardSpacing));
            }
            out.append(shared[GameConsts.globalObjectives - 1].toStringArray()[i])
                    .append(" │\n");
        }

        int playerPadding = ((cardWidth - "player".length()+4) / 2);
        int sharedPadding = (cardWidth * GameConsts.globalObjectives + cardSpacing - "shared".length()) / 2+1;

        out.append(" ".repeat(marginSide))
                .append("│")
                .append(" ".repeat((cardWidth + cardSpacing/2) +1))
                .append("│")
                .append(" ".repeat((cardSpacing/2 + cardSpacing + 2 * cardWidth)+1))
                .append("│\n");

        out.append(" ".repeat(marginSide))
                .append("╰")
                .append("─".repeat((cardWidth + cardSpacing/2) +1))
                .append("┴")
                .append("─".repeat((cardSpacing/2 + cardSpacing + 2 * cardWidth)+1))
                .append("╯")
                .append("\n");

        out.append("\n".repeat(marginTop));
        
        return out.toString().formatted(
                " ".repeat(playerPadding), "player", " ".repeat(playerPadding),
                " ".repeat(sharedPadding), "shared", " ".repeat(sharedPadding)
        );
    }
}
