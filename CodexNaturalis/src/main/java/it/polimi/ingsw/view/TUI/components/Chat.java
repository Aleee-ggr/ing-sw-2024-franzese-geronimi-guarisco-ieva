package it.polimi.ingsw.view.TUI.components;

import java.util.ArrayList;

public class Chat implements Component {
    public static final int chatHeight = 8;
    public static final int chatWidth = 63;
    private final FixedSizeList<String> chat = new FixedSizeList<>(chatHeight);

    @Override
    public String[] toStringArray() {
        ArrayList<String> out = new ArrayList<>(chatHeight);

        int row;
        for (row = 0; row < chat.currentSize();row++) {
            out.add(
                    String.format("%"+chatWidth+"s", chat.get(row).substring(0, chatWidth))
            );
        }
        for (; row < chatHeight; row++) {
            out.add(
                    String.format("%" + chatWidth + "s"," ")
            );
        }
        return out.toArray(new String[0]);
    }

    public void add(String message) {
        chat.add(message);
    }
}
