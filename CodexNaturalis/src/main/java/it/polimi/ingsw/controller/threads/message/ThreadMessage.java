package it.polimi.ingsw.controller.threads.message;

import it.polimi.ingsw.controller.threads.Status;

public record ThreadMessage(Status status, String message) {
    public static final String draw = "{\"type\":\"draw\",\"player\":\"%s\",\"position\":%d}";
    public static final String response_card = "{\"type\":\"card\",\"id\":%d}";
    public static final String place_card = "{\"type\":\"place\",\"player\":\"%s\",\"x\":%d,\"y\":%d,\"id\":%d}";
    public static final String join = "{\"type\":\"join\",\"name\":\"%s\"}";
}
