package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.view.TUI.Compositor;

public class TopBar {
    private String message = "";

    public synchronized void setMessage(String message) {
        this.message = message;
    }

    @Override
    public synchronized String toString() {
        return "\u001b[1;47;30m" + message + " ".repeat(Compositor.screenWidth - message.length() + 1) + "\u001b[0m";
    }
}
