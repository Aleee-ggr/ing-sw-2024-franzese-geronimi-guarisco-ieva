package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientInterface;

import java.util.ArrayList;

public class Chat implements Component {
    public static final int chatHeight = 8;
    public static final int chatWidth = 63;
    private final FixedSizeList<String> chat = new FixedSizeList<>(chatHeight);
    private final ClientInterface client;

    public Chat(ClientInterface client) {
        this.client = client;
    }

    @Override
    public String[] toStringArray() {
        ArrayList<String> out = new ArrayList<>(chatHeight);
        getChat();

        int row;
        for (row = 0; row < chat.currentSize();row++) {
            out.add(
                    String.format("%-"+chatWidth+"s", chat.get(row).substring(0, Math.min(chatWidth, chat.get(row).length())))
            );
        }
        for (; row < chatHeight; row++) {
            out.add(
                    String.format("%" + chatWidth + "s"," ")
            );
        }
        return out.toArray(new String[0]);
    }

    public void getChat() {
        if (client.getChat() == null) {
            return;
        }
        chat.clear();
        for (ChatMessage msg : client.getChat()) {
            chat.add(msg.toString());
        }
    }
}
