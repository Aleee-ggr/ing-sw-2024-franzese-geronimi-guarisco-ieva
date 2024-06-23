package it.polimi.ingsw.view.TUI.components;

/**
 * Represents a prompt component in the TUI that displays a username followed by a prompt symbol (>).
 * Implements the {@link Component} interface.
 */
public class Prompt implements Component {
    private final String username;
    private final String firstPlayer;

    /**
     * Constructs a Prompt object initialized with a username.
     *
     * @param username The username to be displayed in the prompt.
     */
    public Prompt(String username, String firstPlayer) {
        this.username = username;
        this.firstPlayer = firstPlayer;
    }

    /**
     * Converts the Prompt object to its string representation.
     * The string representation consists of the username followed by a prompt symbol (>) and is formatted accordingly.
     *
     * @return The string representation of the Prompt object.
     */
    @Override
    public String toString() {
        if(firstPlayer.equals(username))
            return "\u001b[1;90m%s> \u001b[0m".formatted(username);
        return "%s> ".formatted(username);
    }
}
