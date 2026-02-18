package org.kevinkib.statistics.business.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Hand(List<Rank> ranks) {

    public static int MAX_HAND_VALUE = 21;
    public static int ACE_LOW_VALUE = 1;

    public Hand {
        ranks = List.copyOf(ranks);
    }

    public Hand(Rank... ranks) {
        this(Arrays.asList(ranks));
    }

    public Hand add(Rank rank) {

        List<Rank> newRanks = new ArrayList<>(this.ranks);
        newRanks.add(rank);

        return new Hand(newRanks);
    }

    public int score() {
        int scoreWithHighAces = ranks.stream()
                .mapToInt(Rank::value)
                .sum();

        long numberOfAces = ranks.stream()
                .filter(Rank.ACE::equals)
                .count();

        int score = scoreWithHighAces;
        int lowAces = 0;

        while(score > MAX_HAND_VALUE && lowAces < numberOfAces) {
            score -= (Rank.ACE.value() - ACE_LOW_VALUE);
            ++lowAces;
        }

        return score;
    }

    public boolean isBust() {
        return score() > MAX_HAND_VALUE;
    }

}