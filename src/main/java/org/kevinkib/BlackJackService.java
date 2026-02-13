package org.kevinkib;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.Deck;
import org.kevinkib.cards.domain.DeckType;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;
import org.kevinkib.cards.domain.french.FrenchRank;
import org.kevinkib.cards.domain.french.FrenchSuit;
import org.springframework.jdbc.core.JdbcTemplate;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlackJackService {

    private Deck deck;
    private FrenchDeckFactory deckFactory;
    private final Logger logger = new Logger();
    private final JdbcTemplate jdbcTemplate;
    private Long gameId = 0L;
    private GameState gameState;

    public static final String JDBC_URL = "jdbc:h2:file:./src/main/data/resources";
    public static final String JDBC_USERNAME = "sa";
    public static final String JDBC_PASSWORD = "";
    public static HikariDataSource dataSource;

    private List<Card> playerCards;
    private List<Card> dealerCards;

    public BlackJackService(FrenchDeckFactory deckFactory) {
        this.deckFactory = deckFactory;

        this.jdbcTemplate = new JdbcTemplate(getDataSource());
    }

    public static HikariDataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }

        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(JDBC_URL);
        dataSource.setUsername(JDBC_USERNAME);
        dataSource.setPassword(JDBC_PASSWORD);

        return dataSource;
    }

    public GameState createGame() {
        deck = deckFactory.generate(DeckType.FRENCH);
        logger.write("New game");
        createGameInDatabase();

        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();

        playerDraw();
        dealerDraw();

        gameState = GameState.ONGOING;

        return gameState;
    }

    private void createGameInDatabase() {
        String sql = "INSERT INTO GAME (GAME_CREATION_DATE, GAME_STATE) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ps.setString(2, GameState.ONGOING.name());
            return ps;
        }, keyHolder);

        gameId = keyHolder.getKey().longValue();
    }

    public GameState hit() {
        playerDraw();

        if (sumCards(playerCards) > 21) {
            logger.write("Player exceeded 21 and lost");
            gameState = GameState.LOSE;
        }

        if (sumCards(dealerCards) < 17) {
            dealerDraw();
        }
        if (sumCards(dealerCards) > 21) {
            logger.write("Dealer exceeded 21. Player wins");
            gameState = GameState.WIN;
        }

        else if (sumCards(dealerCards) == 21 && sumCards(playerCards) == 21) {
            logger.write("Player and dealer reached 21. Tie");
            gameState = GameState.TIE;
        }
        else if (sumCards(dealerCards) == 21 && sumCards(playerCards) < 21) {
            logger.write("Dealer reached 21. Player lost");
            gameState = GameState.LOSE;
        }
        else if (sumCards(dealerCards) < 21 && sumCards(playerCards) == 21) {
            logger.write("Player reached 21 and wins");
            gameState = GameState.WIN;
        }

        return gameState;
    }

    public GameState stand() {
        logger.write("Player stands.");

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

    public Long getGameId() {
        return gameId;
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
        Card cardDrawn = playerCards.get(playerCards.size()-1);
        logger.write("Player drawed "+cardDrawn.getRank()+". Score: "+sumCards(playerCards));
        saveMoveInDatabase(false, cardDrawn);
        return drawed;
    }

    public boolean dealerDraw() {
        boolean drawed = draw(dealerCards);
        Card cardDrawn = dealerCards.get(dealerCards.size()-1);
        logger.write("Dealer drawed "+cardDrawn.getRank()+". Score: "+sumCards(dealerCards));
        saveMoveInDatabase(true, cardDrawn);
        return drawed;
    }

    private void saveMoveInDatabase(boolean isDealer, Card cardDrawn) {
        jdbcTemplate.update(
                "INSERT INTO PILE(PILE_FK_GAME, PILE_PLAYER_ID, PILE_CARD_RANK, PILE_CARD_COLOR) " +
                        "VALUES(?,?,?,?);",
                gameId,
                isDealer ? 0 : 1,
                cardDrawn.getRank().getStrength(),
                cardDrawn.getSuit().toString()
        );
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

    public void loadGame(Long gameId) {
        GameDB gameDB = jdbcTemplate.queryForObject("SELECT * FROM GAME WHERE GAME_ID = ?", new Object[]{gameId},
                (rs, rowNum) -> new GameDB(
                        rs.getLong("GAME_ID"),
                        rs.getDate("GAME_CREATION_DATE"),
                        rs.getString("GAME_STATE")
                ));

        List<PileDB> pilesDB = jdbcTemplate.query("SELECT * FROM PILE WHERE PILE_FK_GAME = ?", new Object[]{gameId},
                (rs, rowNum) -> new PileDB(
                        rs.getLong("PILE_ID"),
                        rs.getInt("PILE_PLAYER_ID"),
                        rs.getInt("PILE_CARD_RANK"),
                        rs.getString("PILE_CARD_COLOR")
                ));

        if (gameDB == null) {
            return;
        }

        deck = deckFactory.generate(DeckType.FRENCH);
        logger.write("Loaded game "+gameId);

        this.gameId = gameId;
        gameState = GameState.from(gameDB.state());
        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();
        for (PileDB pileDB : pilesDB) {
            if (pileDB.playerId() == 0L) {
                dealerCards.add(new Card(
                        FrenchRank.fromStrength(pileDB.cardRank()),
                        FrenchSuit.from(pileDB.cardColor())
                ));
            }
            else {
                playerCards.add(new Card(
                        FrenchRank.fromStrength(pileDB.cardRank()),
                        FrenchSuit.from(pileDB.cardColor())
                ));
            }
        }
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
