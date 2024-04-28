package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
import it.polimi.ingsw.helpers.parsers.ObjectiveParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.view.TUI.components.ObjectiveCard;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

public class ObjectiveCardTest {
    @Test
    public void testPattern() throws IOException, JsonFormatException {
        Deck<Objective> deck = new ObjectiveParser().readFile(Path.of(GameConsts.cardJsonPath)).parse();
        Objective obj = deck.draw();

        ObjectiveCard card = new ObjectiveCard(obj);
        System.out.println(card);
    }

    @Test
    public void testResource() throws IOException, JsonFormatException {
        Deck<Objective> deck = new ObjectiveParser().readFile(Path.of(GameConsts.cardJsonPath)).parse();
        Objective obj = deck.draw();
        while (obj.getType().equals("pattern")) {
            obj = deck.draw();
        }

        ObjectiveCard card = new ObjectiveCard(obj);
        System.out.println(card);
    }
}
