package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.enums.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class ColorView implements Component {
    public static final int width = 166;
    public static final int height = 39;

    private final static String HEADER = "CHOOSE YOUR COLOR";

    private final ArrayList<Color> availableColors;

    private final HashMap<Integer, Integer> cardSpacing;
    private final HashMap<Integer, Integer> leftSpacing;
    private final HashMap<Integer, Integer> rightSpacing;
    private final int cardHeight;
    private final int cardWidth;

    private final int contentWidth;
    private final int marginSide;

    private final int contentHeight;
    private final int marginTop;

    public ColorView(ArrayList<Color> availableColors) {
        this.availableColors = availableColors;
        cardHeight = 2;
        cardWidth = 4;

        contentWidth = 39;
        marginSide = (width - contentWidth) / 2;

        contentHeight = 5 + cardHeight;
        marginTop = (height - contentHeight) / 2;

        cardSpacing = new HashMap<>();
        leftSpacing = new HashMap<>();
        rightSpacing = new HashMap<>();
        cardSpacing.put(1, 0);
        cardSpacing.put(2, 15);
        cardSpacing.put(3, 10);
        cardSpacing.put(4, 5);
        leftSpacing.put(1, 18);
        leftSpacing.put(2, 8);
        leftSpacing.put(3, 4);
        leftSpacing.put(4, 4);
        rightSpacing.put(1, 17);
        rightSpacing.put(2, 8);
        rightSpacing.put(3, 3);
        rightSpacing.put(4, 4);
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
                .append(" ".repeat(leftSpacing.get(availableColors.size()) - 2));

        for (int i = 1; i <= availableColors.size(); i++) {
            out.append(i)
                    .append(".");
            if (i < availableColors.size()) {
                out.append(" ".repeat(cardWidth + cardSpacing.get(availableColors.size()) - 2));
            }
        }
        out.append(" ".repeat(cardWidth + rightSpacing.get(availableColors.size())))
                .append("│\n");

        for (int i = 0; i < cardHeight; i++) {
            out.append(" ".repeat(marginSide))
                    .append("│")
                    .append(" ".repeat(leftSpacing.get(availableColors.size())));
            for (int j = 0; j < availableColors.size(); j++) {
                String colorCode = getColorCode(availableColors.get(j));
                out.append(colorCode).append("█").append(colorCode).append("█").append("\u001b[0m")
                        .append(colorCode).append("█").append(colorCode).append("█").append("\u001b[0m");
                if (j < availableColors.size() - 1) {
                    out.append(" ".repeat(cardSpacing.get(availableColors.size())));
                }
            }
            out.append(" ".repeat(rightSpacing.get(availableColors.size())))
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

    private String getColorCode(Color color) {
        return switch (color) {
            case RED -> "\u001b[1;31m";
            case BLUE -> "\u001b[1;34m";
            case GREEN -> "\u001b[1;32m";
            case YELLOW -> "\u001b[1;33m";
            default -> "\u001b[0m"; // Default to reset if color not found
        };
    }
}