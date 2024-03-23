package it.polimi.ingsw.helpers;

import it.polimi.ingsw.model.cards.GoldCard;

public abstract class CardFactory {
    public static GoldCard emptyGoldCard() {
        return new GoldCard(-1, null, null, null, null); //TODO fix this
    }
}
