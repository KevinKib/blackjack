package org.kevinkib.fake;

import org.kevinkib.cards.domain.CardState;
import org.kevinkib.cards.domain.Deck;
import org.kevinkib.cards.domain.DeckType;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;

public class FrenchDeckFactoryFake extends FrenchDeckFactory {

    private final Deck deck;

    public FrenchDeckFactoryFake(Deck deck) {
        this.deck = deck;
    }

    public Deck generate(DeckType deckType, CardState cardState) {
        return deck;
    }
}
