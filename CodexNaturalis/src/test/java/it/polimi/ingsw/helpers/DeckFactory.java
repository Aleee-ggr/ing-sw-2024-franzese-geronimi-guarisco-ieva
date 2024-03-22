package it.polimi.ingsw.helpers;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;

import java.util.ArrayList;

public abstract class DeckFactory {
    public static Deck<GoldCard> emptyGold() {
        ArrayList<GoldCard> empty = new ArrayList<>();
        return new Deck<GoldCard>(empty);
    }

    public static Deck<StdCard> emptyStd() {
        ArrayList<StdCard> empty = new ArrayList<>();
        return new Deck<StdCard>(empty);
    }
}
