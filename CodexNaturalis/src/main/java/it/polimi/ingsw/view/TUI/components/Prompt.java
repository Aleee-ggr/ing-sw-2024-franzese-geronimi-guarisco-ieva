package it.polimi.ingsw.view.TUI.components;

public class Prompt implements Component {
    private final String username;

    public Prompt(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "%s> ".formatted(username);
    }
}
