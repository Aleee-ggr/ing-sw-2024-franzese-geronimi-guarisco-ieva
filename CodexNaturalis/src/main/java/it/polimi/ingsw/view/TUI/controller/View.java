package it.polimi.ingsw.view.TUI.controller;

public enum View {
    BOARD,
    DECK,
    OBJECTIVES;

    public static View getView(String view) throws IllegalStateException {
        return switch (view.trim().toUpperCase()) {
            case "BOARD"->BOARD;
            case "DECK"->DECK;
            case "OBJECTIVES"->OBJECTIVES;
            default -> throw new IllegalStateException("Unexpected value: " + view.trim().toUpperCase());
        };
    }
}
