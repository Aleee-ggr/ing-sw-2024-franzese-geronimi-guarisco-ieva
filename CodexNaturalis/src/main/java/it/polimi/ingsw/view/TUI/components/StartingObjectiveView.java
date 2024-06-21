package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.view.TUI.components.printables.ObjectiveCard;
import it.polimi.ingsw.view.TUI.components.printables.PrintCards;

public class StartingObjectiveView implements Component {
    public static final int width = 166;
    public static final int height = 24;

    private final static String HEADER = "CHOOSE YOUR OBJECTIVE";
    private final static String HANDHEADER = "YOUR HAND";
    private final static String COMMONHEADER = "COMMON OBJECTIVES";

    private final ObjectiveCard[] commonObjectives;
    private final ObjectiveCard[] privateObjectives;

    private final int cardSpacing = 5;
    private final int cardHeight;
    private final int cardWidth;

    private final int contentWidthDouble;
    private final int contentWidthTriple;
    private final int marginSideDouble;
    private final int tripleMargin;

    private final int[] objectiveIds;

    private final PrintCards[] printCards;

    public StartingObjectiveView(PlayerData playerData) {
        this.privateObjectives = playerData.getStartingObjectives().stream().map(ObjectiveCard::new).toArray(ObjectiveCard[]::new);
        this.commonObjectives = playerData.getGlobalObjectives().stream().map(ObjectiveCard::new).toArray(ObjectiveCard[]::new);
        objectiveIds = new int[]{
                privateObjectives[0].id,
                privateObjectives[1].id,
        };
        printCards = new PrintCards[playerData.getClientHand().size()];

        for (int i = 0; i < playerData.getClientHand().size(); i++) {
            printCards[i] = new PrintCards(playerData.getClientHand().get(i));
        }


        cardHeight = ObjectiveCard.height;
        cardWidth = ObjectiveCard.width;

        contentWidthDouble = ((GameConsts.objectivesToChooseFrom) * cardWidth) + (cardSpacing) + 4;
        marginSideDouble = (width - contentWidthDouble) / 2;

        tripleMargin = (width - (cardWidth * 5 + cardSpacing * 5) + 3) / 2;
        contentWidthTriple = ((GameConsts.firstHandDim) * cardWidth) + (cardSpacing * 3 - 1);
    }

    @Override
    public String toString() {

        int header_padding = (contentWidthDouble - HEADER.length()) / 2;
        int headerHand_padding = (contentWidthTriple - HANDHEADER.length()) / 2;
        int headerCommonObjectives_padding = (contentWidthDouble - COMMONHEADER.length()) / 2;

        StringBuilder out = new StringBuilder();

        out.append(" ".repeat(tripleMargin))
                .append("╭")
                .append("─".repeat(contentWidthTriple))
                .append("┬")
                .append("─".repeat(contentWidthDouble))
                .append("╮")
                .append("\n")
                .append(" ".repeat(tripleMargin))
                .append("│")
                .append(" ".repeat(headerHand_padding))
                .append(HANDHEADER)
                .append(" ".repeat(headerHand_padding))
                .append("│")
                .append(" ".repeat(headerCommonObjectives_padding))
                .append(COMMONHEADER)
                .append(" ".repeat(headerCommonObjectives_padding))
                .append("│")
                .append("\n")
                .append(" ".repeat(tripleMargin))
                .append("├")
                .append("─".repeat(contentWidthTriple))
                .append("┼")
                .append("─".repeat(contentWidthDouble))
                .append("┤")
                .append("\n");


        for (int i = 0; i < PrintCards.height; i++) {
            out.append(" ".repeat((tripleMargin)))
                    .append("│")
                    .append(" ".repeat(cardSpacing / 2));

            out.append(printCards[0].toStringArrayColor()[i])
                    .append(" ".repeat(cardSpacing))
                    .append(printCards[1].toStringArrayColor()[i])
                    .append(" ".repeat(cardSpacing))
                    .append(printCards[2].toStringArrayColor()[i])
                    .append(" ".repeat(cardSpacing / 2))
                    .append("│");


            if (i == 0 || i == PrintCards.height - 1) {
                out.append(" ".repeat(contentWidthDouble));
            } else {
                out.append(" ".repeat(cardSpacing / 2))
                        .append(commonObjectives[0].toStringArrayColor()[i - 1])
                        .append(" ".repeat(cardSpacing))
                        .append(commonObjectives[1].toStringArrayColor()[i - 1])
                        .append(" ".repeat(cardSpacing / 2));
            }
            out.append("│")
                    .append("\n");
        }


        out.append(" ".repeat(tripleMargin))
                .append("╰")
                .append("─".repeat(contentWidthTriple))
                .append("┴")
                .append("─".repeat(contentWidthDouble))
                .append("╯")
                .append("\n");


        out.append(" ".repeat(marginSideDouble))
                .append("╭")
                .append("─".repeat(contentWidthDouble))
                .append("╮")
                .append("\n")
                .append(" ".repeat(marginSideDouble))
                .append("│")
                .append(" ".repeat(header_padding))
                .append(HEADER)
                .append(" ".repeat(header_padding))
                .append("│")
                .append("\n")
                .append(" ".repeat(marginSideDouble))
                .append("├")
                .append("─".repeat(contentWidthDouble))
                .append("┤")
                .append("\n")
                .append(" ".repeat(marginSideDouble))
                .append("│")
                .append("1.")
                .append(" ".repeat(((contentWidthDouble - 4) / 2) + 1))
                .append("2.")
                .append(" ".repeat((contentWidthDouble - 4) / 2))
                .append("│")
                .append("\n");
        for (int i = 0; i < cardHeight; i++) {
            out.append(" ".repeat(marginSideDouble))
                    .append("│")
                    .append(" ".repeat(cardSpacing / 2))
                    .append(privateObjectives[0].toStringArrayColor()[i])
                    .append(" ".repeat(cardSpacing))
                    .append(privateObjectives[1].toStringArrayColor()[i])
                    .append(" ".repeat(cardSpacing / 2))
                    .append("│\n");
        }
        out.append(" ".repeat(marginSideDouble))
                .append("│")
                .append(" ".repeat(contentWidthDouble))
                .append("│\n")
                .append(" ".repeat(marginSideDouble))
                .append("╰")
                .append("─".repeat(contentWidthDouble))
                .append("╯")
                .append("\n");

        return out.toString();
    }
}
