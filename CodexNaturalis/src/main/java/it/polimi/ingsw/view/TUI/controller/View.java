package it.polimi.ingsw.view.TUI.controller;

/**
 * The View enum represents the different views that can be displayed in the TUI.
 * It includes methods for converting a string representation of a view into a View enum constant.
 */
public enum View {
    BOARD,
    DECK,
    OBJECTIVES;

    /**
     * Returns the View enum constant corresponding to the specified string representation of a view.
     *
     * @param view The string representation of the view.
     * @return The View enum constant corresponding to the specified string.
     * @throws IllegalStateException If the specified string does not match any of the view names.
     */
    public static View getView(String view) throws IllegalStateException {
        return switch (view.trim().toUpperCase()) {
            case "BOARD"->BOARD;
            case "DECK"->DECK;
            case "OBJECTIVES"->OBJECTIVES;
            default -> throw new IllegalStateException("Unexpected value: " + view.trim().toUpperCase());
        };
    }
}
