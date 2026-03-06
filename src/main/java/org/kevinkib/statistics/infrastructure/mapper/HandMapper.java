package org.kevinkib.statistics.infrastructure.mapper;

import org.kevinkib.BlackJackService;
import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.french.FrenchRank;
import org.kevinkib.cards.domain.french.FrenchSuit;
import org.kevinkib.statistics.business.model.Hand;
import org.kevinkib.statistics.infrastructure.entity.PileDB;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandMapper {

    public static Hand mapGamePileToHand(List<PileDB> pilesDB) {

        List<Card> cards = pilesDB.stream().map(pileDB -> new Card(
                FrenchRank.fromStrength(pileDB.cardRank()),
                FrenchSuit.from(pileDB.cardColor()))
        ).toList();

        int score = BlackJackService.calculateScore(cards);
        int nbCards = pilesDB.size();

        return new Hand(score, nbCards);
    }

    public static Map<Long, Hand> mapPilesFromDifferentGames(List<PileDB> pilesDB) {

        Map<Long, Hand> hands = pilesDB.stream()
                .collect(Collectors.groupingBy(PileDB::gameId))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> mapGamePileToHand(entry.getValue())
                ));

        return hands;
    }

}
