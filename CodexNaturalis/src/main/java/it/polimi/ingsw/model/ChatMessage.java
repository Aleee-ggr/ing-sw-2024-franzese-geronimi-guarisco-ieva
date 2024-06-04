package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.function.Predicate;

public record ChatMessage(String sender, String receiver, String message) implements Serializable {

    public boolean filterPlayer(String player) {
        return sender.equals(player) || receiver == null || receiver.equals(player);
    }

    public static Predicate<ChatMessage> getPlayerFilter(String player) {
        return (ChatMessage message) -> message.filterPlayer(player);
    }

    @Override
    public String toString() {
        return sender + ": " + message;
    }
}
