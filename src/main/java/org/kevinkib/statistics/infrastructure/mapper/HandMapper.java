package org.kevinkib.statistics.infrastructure.mapper;

import jakarta.annotation.Nonnull;
import org.kevinkib.LegacyBlackJackService;
import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.french.FrenchRank;
import org.kevinkib.cards.domain.french.FrenchSuit;
import org.kevinkib.statistics.infrastructure.entity.CardDB;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandMapper {

    public static Hand mapGamePileToHand(List<CardDB> pilesDB) {

        List<Card> cards = getCards(pilesDB);

        int score = LegacyBlackJackService.calculateScore(cards);

        return new Hand(score, pilesDB.size());
    }








    public static Map<Long, Hand> mapPilesFromDifferentGames(List<CardDB> pilesDB) {

        Map<Long, Hand> hands = pilesDB.stream()
                .collect(Collectors.groupingBy(CardDB::gameId))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> mapGamePileToHand(entry.getValue())
                ));

        return hands;
    }

    @Nonnull
    private static List<Card> getCards(List<CardDB> pilesDB) {
        List<Card> cards = pilesDB.stream().map(cardDB -> new Card(
                FrenchRank.fromStrength(cardDB.cardRank()),
                FrenchSuit.from(cardDB.cardColor()))
        ).toList();
        return cards;
    }

}
