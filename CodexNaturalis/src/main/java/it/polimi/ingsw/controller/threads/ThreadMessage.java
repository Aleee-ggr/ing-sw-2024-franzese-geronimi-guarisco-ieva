package it.polimi.ingsw.controller.threads;

public record ThreadMessage(Status status, String message, String[] params) {
}
