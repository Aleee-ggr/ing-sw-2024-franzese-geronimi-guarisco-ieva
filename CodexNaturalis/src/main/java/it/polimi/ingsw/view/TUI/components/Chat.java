package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.Compositor;

import java.util.ArrayList;

public class Chat implements Component {
    public static final int chatHeight = 8;
    public int chatWidth;
    private final FixedSizeList<ChatMessage> chat = new FixedSizeList<>(chatHeight);
    private final ClientInterface client;

    public Chat(ClientInterface client) {
        this.client = client;
        this.chatWidth = Compositor.screenWidth - client.getPlayerNum() * MiniBoard.boardWidth - ResourceView.width;
    }

    @Override
    public String[] toStringArray() {
        ArrayList<String> out = new ArrayList<>(chatHeight);
        getChat();

        int row;
        for (row = 0; row < chat.currentSize(); row++) {
            StringBuilder line = new StringBuilder();
            String message = chat.get(row).message();
            String sender = chat.get(row).sender();
            int senderLength = sender.length();

            if (chat.get(row).receiver() != null) {
                line.append("\u001b[1;90m");
            }
            line.append(sender);
            if (chat.get(row).receiver() != null) {
                line.append("\u001b[0m");
            }
            line.append("> ");

            int totalWidth = chatWidth - (senderLength + 2);
            line.append(String.format("%-" + totalWidth + "s", message.substring(0, Math.min(totalWidth, message.length()))));

            out.add(line.toString());
        }
        for (; row < chatHeight; row++) {
            out.add(String.format("%" + chatWidth + "s", " "));
        }
        return out.toArray(new String[0]);
    }

    public void getChat() {
        if (client.getChat() == null) {
            return;
        }
        chat.clear();
        for (ChatMessage msg : client.getChat()) {
            chat.add(msg);
        }
    }
}
