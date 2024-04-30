package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
import it.polimi.ingsw.helpers.parsers.ObjectiveParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.view.TUI.components.printables.ObjectiveCard;
import it.polimi.ingsw.view.TUI.components.ObjectiveView;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class ObjectiveCardTest {
    @Test
    public void testPattern() throws IOException, JsonFormatException {
        Deck<Objective> deck = new ObjectiveParser().readFile(Path.of(GameConsts.cardJsonPath)).parse();
        Objective obj = deck.draw();

        ObjectiveCard card = new ObjectiveCard(obj);
        System.out.println(card);
    }

    @Test
    public void testResources() throws IOException, JsonFormatException {
        Deck<Objective> deck = new ObjectiveParser().readFile(Path.of(GameConsts.cardJsonPath)).parse();
        Objective obj = deck.draw();
        while (obj.getId() != 99) {
            obj = deck.draw();
        }

        ObjectiveCard card = new ObjectiveCard(obj);
        System.out.println(card);
    }

    @Test
    public void testObjectiveView()throws IOException, JsonFormatException {
        Deck<Objective> deck = new ObjectiveParser().readFile(Path.of(GameConsts.cardJsonPath)).parse();
        ObjectiveCard personal = new ObjectiveCard(deck.draw());
        ObjectiveCard[] shared = new ObjectiveCard[GameConsts.globalObjectives];

        for (int i = 0; i < GameConsts.globalObjectives; i++) {
            shared[i] = new ObjectiveCard(deck.draw());
        }

        ObjectiveView view = new ObjectiveView(personal, shared);

        System.out.println(view);
    }
}
