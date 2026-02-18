package org.kevinkib;

import org.kevinkib.cards.domain.french.FrenchDeckFactory;

public class Main {
    public static void main(String[] args) {
        new BlackJackService(new FrenchDeckFactory()).startGUI();
    }
}