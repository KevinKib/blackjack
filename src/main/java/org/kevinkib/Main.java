package org.kevinkib;

import org.kevinkib.cards.domain.french.FrenchDeckFactory;

public class Main {
    public static void main(String[] args) {
        new LegacyBlackJackService(LegacyBlackJackService.getDataSource(), new FrenchDeckFactory()).startGUI();
    }
}