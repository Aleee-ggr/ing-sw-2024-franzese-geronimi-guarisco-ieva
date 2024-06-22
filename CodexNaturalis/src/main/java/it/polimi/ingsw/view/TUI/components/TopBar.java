package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.view.TUI.Compositor;

/**
 * Represents a top bar component in TUI.
 * The top bar displays a message with specific formatting.
 */
public class TopBar {
    private String message = "";

    /**
     * Sets the message to be displayed in the top bar.
     * Synchronized to ensure thread safety when setting the message.
     *
     * @param message The message to be displayed
     */
    public synchronized void setMessage(String message) {
        this.message = message;
    }

    @Override
    public synchronized String toString() {
        return "\u001b[1;47;30m" + message + " ".repeat(Compositor.screenWidth - message.length() + 1) + "\u001b[0m";
    }
}
