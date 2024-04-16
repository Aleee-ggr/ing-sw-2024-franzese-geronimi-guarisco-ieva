package it.polimi.ingsw.controller.threads.message;

import it.polimi.ingsw.controller.threads.Status;

public record ThreadMessage(Status status, String message) {
    public static final String draw_message = "{\"type\":\"draw\",\"position\":%d}";
    public static final String response_card = "{\"type\":\"card\",\"id\":%d}";
}
