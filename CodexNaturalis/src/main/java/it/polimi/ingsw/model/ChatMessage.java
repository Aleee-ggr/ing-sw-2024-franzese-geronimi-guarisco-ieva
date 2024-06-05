package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.function.Predicate;

public record ChatMessage(String sender, String message, String receiver) implements Serializable {

    public static Predicate<ChatMessage> getPlayerFilter(String player) {
        return (ChatMessage message) -> message.filterPlayer(player);
    }

    public boolean filterPlayer(String player) {
        return sender.equals(player) || receiver == null || receiver.equals(player);
    }

    @Override
    public String toString() {
        return sender + ": " + message;
    }
}
