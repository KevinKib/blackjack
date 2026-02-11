package org.kevinkib;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;
import org.kevinkib.cards.domain.french.FrenchRank;
import org.kevinkib.cards.testhelpers.CardBuilder;
import org.kevinkib.cards.testhelpers.DeckBuilder;
import org.kevinkib.fake.FrenchDeckFactoryFake;

import java.util.Arrays;
import java.util.LinkedList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class BlackJackServiceTest {

    private FrenchDeckFactory frenchDeckFactory;
    private BlackJackService blackJackService;

    @Test
    public void givenGame_thenPlayerCanWinByGettingHigherScore() {
        frenchDeckFactory = new FrenchDeckFactoryFake(DeckBuilder.aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.EIGHT).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(frenchDeckFactory);

        blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(10));
        assertThat(blackJackService.getDealerScore(), is(10));

        GameState state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(20));
        assertThat(blackJackService.getDealerScore(), is(18));
        assertThat(state, is(GameState.ONGOING));

        state = blackJackService.stand();
        assertThat(blackJackService.getPlayerScore(), is(20));
        assertThat(blackJackService.getDealerScore(), is(18));
        assertThat(state, is(GameState.WIN));
    }

    @Test
    public void givenGame_thenPlayerCanTie() {
        frenchDeckFactory = new FrenchDeckFactoryFake(DeckBuilder.aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(frenchDeckFactory);

        blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(10));
        assertThat(blackJackService.getDealerScore(), is(10));

        GameState state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(20));
        assertThat(blackJackService.getDealerScore(), is(20));
        assertThat(state, is(GameState.ONGOING));

        state = blackJackService.stand();
        assertThat(blackJackService.getPlayerScore(), is(20));
        assertThat(blackJackService.getDealerScore(), is(20));
        assertThat(state, is(GameState.TIE));
    }

    @Test
    public void givenGame_thenPlayerCanLoseByGettingLowerScore() {
        frenchDeckFactory = new FrenchDeckFactoryFake(DeckBuilder.aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.EIGHT).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(frenchDeckFactory);

        blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(10));
        assertThat(blackJackService.getDealerScore(), is(10));

        GameState state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(18));
        assertThat(blackJackService.getDealerScore(), is(20));
        assertThat(state, is(GameState.ONGOING));

        state = blackJackService.stand();
        assertThat(blackJackService.getPlayerScore(), is(18));
        assertThat(blackJackService.getDealerScore(), is(20));
        assertThat(state, is(GameState.LOSE));
    }

    @Test
    public void givenGame_thenPlayerCanLoseByGettingAbove21() {
        frenchDeckFactory = new FrenchDeckFactoryFake(DeckBuilder.aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.EIGHT).build(),
                        CardBuilder.aCard().withRank(FrenchRank.SEVEN).build(),
                        CardBuilder.aCard().withRank(FrenchRank.SIX).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(frenchDeckFactory);

        blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(10));
        assertThat(blackJackService.getDealerScore(), is(10));

        GameState state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(18));
        assertThat(blackJackService.getDealerScore(), is(17));
        assertThat(state, is(GameState.ONGOING));

        state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(24));
        assertThat(state, is(GameState.LOSE));
    }

    @Test
    public void givenGame_thenPlayerCanWinByLettingDealerGoAbove21() {
        frenchDeckFactory = new FrenchDeckFactoryFake(DeckBuilder.aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        CardBuilder.aCard().withRank(FrenchRank.TWO).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.TWO).build(),
                        CardBuilder.aCard().withRank(FrenchRank.SIX).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(frenchDeckFactory);

        blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(2));
        assertThat(blackJackService.getDealerScore(), is(10));

        GameState state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(4));
        assertThat(blackJackService.getDealerScore(), is(16));
        assertThat(state, is(GameState.ONGOING));

        state = blackJackService.stand();
        assertThat(blackJackService.getPlayerScore(), is(4));
        assertThat(blackJackService.getDealerScore(), is(26));
        assertThat(state, is(GameState.WIN));
    }

    @Test
    public void givenGame_thenPlayerCanWinByGetting21() {
        frenchDeckFactory = new FrenchDeckFactoryFake(DeckBuilder.aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.ACE).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(frenchDeckFactory);

        blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(10));
        assertThat(blackJackService.getDealerScore(), is(10));

        GameState state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(21));
        assertThat(blackJackService.getDealerScore(), is(20));
        assertThat(state, is(GameState.WIN));
    }

    @Test
    public void givenGame_thenPlayerCanLoseByLettingDealerReach21() {
        frenchDeckFactory = new FrenchDeckFactoryFake(DeckBuilder.aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.JACK).build(),
                        CardBuilder.aCard().withRank(FrenchRank.ACE).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(frenchDeckFactory);

        blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(10));
        assertThat(blackJackService.getDealerScore(), is(10));

        GameState state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(20));
        assertThat(blackJackService.getDealerScore(), is(21));
        assertThat(state, is(GameState.LOSE));
    }

}