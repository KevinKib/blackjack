package org.kevinkib;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.Deck;
import org.kevinkib.cards.domain.DeckType;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;
import org.kevinkib.cards.domain.french.FrenchRank;

import java.util.ArrayList;
import java.util.List;

public class BlackJackService {

    private Deck deck;
    private FrenchDeckFactory deckFactory;
    private final Logger logger = new Logger();

    private List<Card> playerCards;
    private List<Card> dealerCards;

    public BlackJackService(FrenchDeckFactory deckFactory) {
        this.deckFactory = deckFactory;
    }

    public GameState createGame() {
        deck = deckFactory.generate(DeckType.FRENCH);
        logger.write("New game");

        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();

        playerDraw();
        dealerDraw();

        saveGameInDatabase();

        return GameState.ONGOING;
    }

    public GameState hit() {
        playerDraw();

        saveGameInDatabase();

        if (sumCards(playerCards) > 21) {
            logger.write("Player exceeded 21 and lost");
            return GameState.LOSE;
        }

        if (sumCards(dealerCards) < 17) {
            dealerDraw();
        }

        if (sumCards(dealerCards) > 21) {
            logger.write("Dealer exceeded 21. Player wins");
            return GameState.WIN;
        }

        if (sumCards(dealerCards) == 21 && sumCards(playerCards) == 21) {
            logger.write("Player and dealer reached 21. Tie");
            return GameState.TIE;
        }
        else if (sumCards(dealerCards) == 21 && sumCards(playerCards) < 21) {
            logger.write("Dealer reached 21. Player lost");
            return GameState.LOSE;
        }
        else if (sumCards(dealerCards) < 21 && sumCards(playerCards) == 21) {
            logger.write("Player reached 21 and wins");
            return GameState.WIN;
        }

        return GameState.ONGOING;
    }

    public GameState stand() {
        logger.write("Player stands.");

        saveGameInDatabase();

        dealerDrawUntil17();

        if (sumCards(dealerCards) > 21) {
            logger.write("Dealer exceeded 21. Player wins");
            return GameState.WIN;
        }
        else if (sumCards(dealerCards) > sumCards(playerCards)) {
            logger.write("Dealer is the closest to 21. Player lost");
            return GameState.LOSE;
        }
        else if (sumCards(dealerCards) == sumCards(playerCards)) {
            logger.write("Player and dealer have the same score. Tie");
            return GameState.TIE;
        }
        else if (sumCards(dealerCards) < sumCards(playerCards)) {
            logger.write("Player is the closest to 21 and wins");
            return GameState.WIN;
        }

        throw new IllegalStateException("Game should be ended if player stands");
    }

    public int getPlayerScore() {
        return sumCards(playerCards);
    }

    public int getDealerScore() {
        return sumCards(dealerCards);
    }

    private void dealerDrawUntil17() {
        while(sumCards(dealerCards) < 17) {
            draw(dealerCards);
        }
    }

    public boolean playerDraw() {
        boolean drawed = draw(playerCards);
        logger.write("Player drawed "+playerCards.get(playerCards.size()-1).getRank()+". Score: "+sumCards(playerCards));
        return drawed;
    }

    public boolean dealerDraw() {
        boolean drawed = draw(dealerCards);
        logger.write("Dealer drawed "+dealerCards.get(dealerCards.size()-1).getRank()+". Score: "+sumCards(dealerCards));
        return drawed;
    }

    private boolean draw(List<Card> cards) {
        Card card = deck.draw();

        cards.add(card);
        if (sumCards(cards) > 21) {
            return false;
        }

        return true;
    }

    private int sumCards(List<Card> cards) {
        int sum = 0;
        for (var card : cards) {
            if (FrenchRank.ACE.equals(card.getRank())) {
                sum += 11;
            }
            else {
                sum += ((FrenchRank)card.getRank()).getValue();
            }
        }
        return sum;
    }

    private void saveGameInDatabase() {

    }

    /*

    règles:
    - un joueur, un croupier; le joueur commence
    - le joueur pioche directement une carte
    - un joueur a pour somme la somme des cartes (têtes à 10, as à 11)
    - si un joueur dépasse 21, il perd
    - sinon, le joueur le plus proche de 21 gagne

     */


}
