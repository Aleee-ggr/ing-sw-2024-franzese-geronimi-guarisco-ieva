package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.view.TUI.components.Chat;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChatTest {
    Chat test;

    @Before
    public void setup() {
        test = new Chat();
    }

    @Test
    public void testEmptyChat() {
        assertEquals(Chat.chatHeight, test.get().length);
        assertEquals(Chat.chatWidth, test.get()[0].length());
    }

    @Test
    public void testColumnOverflow() {
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vel luctus est. Nunc semper tincidunt.";

        for (int i = 0; i < Chat.chatHeight; i++) {
            test.add(message);
        }


        assertEquals(Chat.chatHeight, test.get().length);
        assertEquals(Chat.chatWidth, test.get()[0].length());
    }

    @Test
    public void testRowOverflow() {
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vel luctus est. Nunc semper tincidunt.";

        for (int i = 0; i < Chat.chatHeight * 10; i++) {
            test.add(message);
        }


        assertEquals(Chat.chatHeight, test.get().length);
        assertEquals(Chat.chatWidth, test.get()[0].length());
    }
}
