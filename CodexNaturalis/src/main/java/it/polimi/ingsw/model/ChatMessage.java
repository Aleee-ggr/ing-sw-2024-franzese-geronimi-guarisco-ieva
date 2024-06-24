package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * Represents a chat message with sender, message content, and optional receiver.
 * Provides a method to create a Predicate for filtering messages based on a specific player.
 **/
public record ChatMessage(String sender, String message, String receiver) implements Serializable {

    /**
     * Returns a Predicate that filters ChatMessage objects based on the specified player.
     *
     * @param player The player to filter messages for.
     * @return A Predicate that evaluates to true if the message sender matches the player,
     * or if the message is a broadcast (receiver is null), or if the receiver matches the player.
     */
    public static Predicate<ChatMessage> getPlayerFilter(String player) {
        return (ChatMessage message) -> message.filterPlayer(player);
    }

    /**
     * Checks if this ChatMessage should be included based on the given player.
     *
     * @param player The player to filter messages for.
     * @return true if the sender matches the player, or if the message is a broadcast (receiver is null),
     * or if the receiver matches the player; false otherwise.
     */
    public boolean filterPlayer(String player) {
        return sender.equals(player) || receiver == null || receiver.equals(player);
    }
}
