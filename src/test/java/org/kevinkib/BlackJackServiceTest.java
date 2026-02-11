package org.kevinkib;

import org.junit.jupiter.api.*;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;
import org.kevinkib.cards.domain.french.FrenchRank;
import org.kevinkib.cards.domain.french.FrenchSuit;
import org.kevinkib.cards.testhelpers.CardBuilder;
import org.kevinkib.fake.FrenchDeckFactoryFake;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.LinkedList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.kevinkib.cards.testhelpers.DeckBuilder.aDeck;

class BlackJackServiceTest {

    private FrenchDeckFactory frenchDeckFactory;
    private BlackJackService blackJackService;
    private static JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void before() {
        jdbcTemplate = new JdbcTemplate(BlackJackService.getDataSource());
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.update("DELETE FROM PILE;");
        jdbcTemplate.update("DELETE FROM GAME;");
    }

    private static CardBuilder aCardWithASuit() {
        return CardBuilder.aCard().withSuit(FrenchSuit.DIAMOND);
    }

    @Test
    public void givenGame_thenPlayerCanWinByGettingHigherScore() {
        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.EIGHT).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);

        GameState state = blackJackService.createGame();
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
        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);

        GameState state = blackJackService.createGame();
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
        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.EIGHT).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);

        GameState state = blackJackService.createGame();
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
        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.EIGHT).build(),
                        aCardWithASuit().withRank(FrenchRank.SEVEN).build(),
                        aCardWithASuit().withRank(FrenchRank.SIX).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);

        GameState state = blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(18));
        assertThat(blackJackService.getDealerScore(), is(17));
        assertThat(state, is(GameState.ONGOING));

        state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(24));
        assertThat(state, is(GameState.LOSE));
    }

    @Test
    public void givenGame_thenPlayerCanWinByLettingDealerGoAbove21() {
        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        aCardWithASuit().withRank(FrenchRank.TWO).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.TWO).build(),
                        aCardWithASuit().withRank(FrenchRank.SIX).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);

        GameState state = blackJackService.createGame();
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
        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.ACE).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);

        GameState state = blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(21));
        assertThat(blackJackService.getDealerScore(), is(20));
        assertThat(state, is(GameState.WIN));
    }

    @Test
    public void givenGame_thenPlayerCanLoseByLettingDealerReach21() {
        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.ACE).build()
                ).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);

        GameState state = blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(20));
        assertThat(blackJackService.getDealerScore(), is(21));
        assertThat(state, is(GameState.LOSE));
    }

    @Test
    public void givenGame_thatComesFromDatabase_thenCanBeResumedNormally() {
        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.JACK).build(),
                        aCardWithASuit().withRank(FrenchRank.FIVE).build(),
                        aCardWithASuit().withRank(FrenchRank.FIVE).build()
                ).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);

        GameState state = blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(15));
        assertThat(blackJackService.getDealerScore(), is(15));
        assertThat(state, is(GameState.ONGOING));

        Long gameId = blackJackService.getGameId();

        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(
                        aCardWithASuit().withRank(FrenchRank.FOUR).build(),
                        aCardWithASuit().withRank(FrenchRank.TWO).build()
                ).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);
        blackJackService.loadGame(gameId);
        blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(19));
        state = blackJackService.stand();
        assertThat(blackJackService.getDealerScore(), is(17));
        assertThat(state, is(GameState.WIN));
    }

    @Test
    public void givenGame_thenAcesAreCalculatedCorrectly() {
        frenchDeckFactory = new FrenchDeckFactoryFake(aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        aCardWithASuit().withRank(FrenchRank.EIGHT).build(),
                        aCardWithASuit().withRank(FrenchRank.EIGHT).build(),
                        aCardWithASuit().withRank(FrenchRank.TWO).build(),
                        aCardWithASuit().withRank(FrenchRank.THREE).build(),
                        aCardWithASuit().withRank(FrenchRank.ACE).build(),
                        aCardWithASuit().withRank(FrenchRank.ACE).build(),
                        aCardWithASuit().withRank(FrenchRank.KING).build()
                ))).buildWithoutRandom()
        );

        blackJackService = new BlackJackService(BlackJackService.getTestDataSource(), frenchDeckFactory);

        GameState state = blackJackService.createGame();
        assertThat(blackJackService.getPlayerScore(), is(10));
        assertThat(blackJackService.getDealerScore(), is(11));
        assertThat(state, is(GameState.ONGOING));

        state = blackJackService.hit();
        assertThat(blackJackService.getPlayerScore(), is(21));
        assertThat(blackJackService.getDealerScore(), is(11));
        assertThat(state, is(GameState.ONGOING));

        state = blackJackService.stand();
        assertThat(blackJackService.getPlayerScore(), is(21));
        assertThat(blackJackService.getDealerScore(), is(22));
        assertThat(state, is(GameState.WIN));
    }

}