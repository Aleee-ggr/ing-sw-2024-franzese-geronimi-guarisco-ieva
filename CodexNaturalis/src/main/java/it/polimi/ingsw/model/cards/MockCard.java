package it.polimi.ingsw.model.cards;

public class MockCard extends Card{

    /**
     * Constructor for the Card class.
     *
     * @param id           Unique identifier of the card.
     * @param frontCorners Array of corners on the front side of the card.
     */
    public MockCard(int id, Corner[] frontCorners) {
        super(id, frontCorners, false);
    }
}
