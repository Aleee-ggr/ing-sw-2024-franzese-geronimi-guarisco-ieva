package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatMessageTest {
    private static final String[] messages = new String[]{"Foo", "Bar", "Baz", "Fizz", "Buzz", "FooBar", "FizzBuzz", "Lorem Ipsum", "Dolor Sit Amet", "Odi et Amo. quare id faciam, fortasse requirit.", "Please Intellij stop" + " " + "wrapping this array", "Please I'm begging you",};

    private static final String[] players = new String[]{"player1", "player2", "player3", "player4"};
    
    @Test
    public void testSimpleChatMessage() {
        ArrayList<ChatMessage> chat = new ArrayList<>();
        Map<String, ArrayList<String>> playerMessages = new HashMap<>();

        for (int i = 0; i < messages.length; i++) {
            playerMessages.computeIfAbsent(players[i % players.length], k -> new ArrayList<>());

            playerMessages.get(players[i % players.length]).add(messages[i]);
            chat.add(new ChatMessage(players[i % players.length], messages[i], null));
        }

        for (ChatMessage msg : chat) {
            assertTrue(playerMessages.get(msg.sender()).contains(msg.message()));
        }
    }

    @Test
    public void testChatMessageFilterGlobal() {
        List<ChatMessage> chat = new ArrayList<>();
        Map<String, ArrayList<String>> playerMessages = new HashMap<>();

        for (int i = 0; i < messages.length; i++) {
            playerMessages.computeIfAbsent(players[i % players.length], k -> new ArrayList<>());

            playerMessages.get(players[i % players.length]).add(messages[i]);
            chat.add(new ChatMessage(players[i % players.length], messages[i], null));
        }

        Predicate<ChatMessage> p = ChatMessage.getPlayerFilter(players[0]);
        List<ChatMessage> filtered = chat.stream().filter(p).toList();

        assertEquals(chat, filtered);
    }

    @Test
    public void testChatMessageFilterPlayer1() {
        List<ChatMessage> chat = new ArrayList<>();
        Map<String, List<ChatMessage>> playerMessages = new HashMap<>();

        for (int i = 0; i < messages.length; i++) {
            playerMessages.computeIfAbsent(players[i % players.length], k -> new ArrayList<>());
            ChatMessage m = new ChatMessage(players[i % players.length], messages[i], players[0]);

            playerMessages.get(players[i % players.length]).add(m);
            chat.add(m);
        }

        Predicate<ChatMessage> p = ChatMessage.getPlayerFilter(players[0]);
        List<ChatMessage> filtered = chat.stream().filter(p).toList();
        assertEquals(chat, filtered);

        for (int i = 1; i < players.length; i++) {
            p = ChatMessage.getPlayerFilter(players[i]);
            filtered = chat.stream().filter(p).toList();
            assertEquals(playerMessages.get(players[i]), filtered);
        }

    }
}
