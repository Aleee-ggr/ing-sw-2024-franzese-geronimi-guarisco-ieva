package it.polimi.ingsw.helpers;

import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;

public abstract class CardFactory {
    public static GoldCard emptyGoldCard() {
        return new GoldCard(-1, null, null, null, null);
    }

    public static StdCard emptyStdCard() {
        return new StdCard(-1, null, null, false);
    }
}
