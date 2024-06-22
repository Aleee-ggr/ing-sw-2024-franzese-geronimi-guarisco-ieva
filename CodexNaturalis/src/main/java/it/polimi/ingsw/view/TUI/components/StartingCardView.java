package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.view.TUI.components.printables.ObjectiveCard;
import it.polimi.ingsw.view.TUI.components.printables.PrintCards;

/**
 * Represents the view for displaying starting card faces in a board game.
 * This component displays the front and back sides of a starting card in a formatted manner suitable for the TUI.
 */
public class StartingCardView implements Component{
    public static final int width = 166;
    public static final int height = 39;

    private final static String HEADER="STARTING CARD FACES";

    private final StartingCard startingCard;

    private final int cardSpacing = 5;
    private final int cardHeight;
    private final int cardWidth;

    private final int contentWidth;
    private final int marginSide;

    private final int contentHeight;
    private final int marginTop;

    /**
     * Constructs a StartingCardView object based on the provided starting card.
     *
     * @param startingCard The starting card to display.
     */
    public StartingCardView(StartingCard startingCard) {
        this.startingCard = startingCard;
        cardHeight = PrintCards.height;
        cardWidth = PrintCards.width;

        contentWidth = ((GameConsts.objectivesToChooseFrom) * cardWidth) + (cardSpacing) + 4;
        marginSide = (width - contentWidth) / 2;

        contentHeight = 5 + cardHeight;
        marginTop = (height - contentHeight) / 2;
    }

    /**
     * Converts the StartingCardView object to its string representation.
     * This representation includes the formatted layout of the starting card with both front and back sides displayed.
     *
     * @return The string representation of the StartingCardView.
     */
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
        for (int i = 1; i < cardHeight; i++) {
            out.append(" ".repeat(marginSide))
                    .append("│")
                    .append(" ".repeat(cardSpacing/2))
                    .append(new PrintCards(startingCard.setFrontSideUp(true)).toStringArrayColor()[i])
                    .append(" ".repeat(cardSpacing))
                    .append(new PrintCards(startingCard.setFrontSideUp(false)).toStringArrayColor()[i])
                    .append(" ".repeat(cardSpacing/2))
                    .append("│\n");
        }
        out.append(" ".repeat(marginSide))
                .append("╰")
                .append("─".repeat(contentWidth))
                .append("╯")
                .append("\n");

        return out.toString();
    }
}
