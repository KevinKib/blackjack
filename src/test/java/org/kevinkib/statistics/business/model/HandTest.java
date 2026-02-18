package org.kevinkib.statistics.business.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class HandTest {

    @Test
    public void canSumRanksCorrectly() {
        assertThat(new Hand(Rank.EIGHT, Rank.FIVE).score(), is(13));
    }

    @Test
    public void canSumFaceCardsCorrectly() {
        assertThat(new Hand(Rank.KING, Rank.FOUR).score(), is(14));
        assertThat(new Hand(Rank.JACK, Rank.SIX).score(), is(16));
    }

    @Test
    public void canSumAcesCorrectly_whenAceCountsAs11() {
        assertThat(new Hand(Rank.ACE, Rank.THREE, Rank.SIX).score(), is(20));
    }

    @Test
    public void canSumAcesCorrectly_whenAceCountsAs1() {
        assertThat(new Hand(Rank.ACE, Rank.JACK, Rank.THREE).score(), is(14));
    }

    @Test
    public void canSumMultipleAcesCorrectly() {
        assertThat(new Hand(Rank.ACE, Rank.ACE, Rank.EIGHT).score(), is(20));
    }
}
