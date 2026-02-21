package org.kevinkib;

import org.kevinkib.cards.domain.french.FrenchDeckFactory;
import org.kevinkib.config.AppConfig;

public class Main {
    public static void main(String[] args) {
        new AppConfig().blackJackService().startGUI();
    }
}