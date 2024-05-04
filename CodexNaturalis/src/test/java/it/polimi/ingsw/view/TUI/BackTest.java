package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.view.TUI.components.printables.CardBack;
import org.junit.Test;

public class BackTest {
    @Test
    public void printBacks() {
        for (String back : CardBack.values) {
            System.out.println(back);
        }
    }
}
