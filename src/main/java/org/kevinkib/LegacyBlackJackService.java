package org.kevinkib;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.Deck;
import org.kevinkib.cards.domain.DeckType;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;
import org.kevinkib.cards.domain.french.FrenchRank;
import org.kevinkib.cards.domain.french.FrenchSuit;
import org.kevinkib.statistics.business.port.in.StatisticsUseCase;
import org.springframework.jdbc.core.JdbcTemplate;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.kevinkib.cards.domain.french.FrenchRank.ACE;

public class LegacyBlackJackService {

    private Deck deck;
    private FrenchDeckFactory deckFactory;
    private final Logger logger = new Logger(false);
    private final JdbcTemplate jdbcTemplate;
    private Long gameId = 0L;
    private LegacyGameState gameState;

    public static final String JDBC_URL = "jdbc:h2:file:./src/main/data/demo/resources";
    public static final String JDBC_TEST_URL = "jdbc:h2:file:./src/main/data/test/resources";
    public static final String JDBC_USERNAME = "sa";
    public static final String JDBC_PASSWORD = "";
    public static HikariDataSource dataSource;

    private final Scanner scanner;

    private List<Card> playerCards;
    private List<Card> dealerCards;

    private final StatisticsUseCase statistics;

    public LegacyBlackJackService(HikariDataSource dataSource, FrenchDeckFactory deckFactory,
                                  StatisticsUseCase statistics) {
        this.deckFactory = deckFactory;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.scanner = new Scanner(System.in);
        this.statistics = statistics;
    }

    public void startGUI() {

        boolean stillPlay = true;

        do {
            System.out.println();
            System.out.println("██████╗ ██╗      █████╗  ██████╗██╗  ██╗     ██╗ █████╗  ██████╗██╗  ██╗");
            System.out.println("██╔══██╗██║     ██╔══██╗██╔════╝██║ ██╔╝     ██║██╔══██╗██╔════╝██║ ██╔╝");
            System.out.println("██████╔╝██║     ███████║██║     █████╔╝      ██║███████║██║     █████╔╝ ");
            System.out.println("██╔══██╗██║     ██╔══██║██║     ██╔═██╗ ██   ██║██╔══██║██║     ██╔═██╗ ");
            System.out.println("██████╔╝███████╗██║  ██║╚██████╗██║  ██╗╚█████╔╝██║  ██║╚██████╗██║  ██╗");
            System.out.println("╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝ ╚════╝ ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝");
            System.out.println();

            printStatistics();

            createGame();

            if (isGameOver(gameState)) {
                printFinalState(gameState);
                continue;
            }

            boolean running = true;

            while (running) {
                printScores();

                System.out.println("\nChoose an action:");
                System.out.println("1 - Hit");
                System.out.println("2 - Stand");
                System.out.print("> ");

                String input = scanner.nextLine();

                switch (input) {

                    case "1" -> {
                        LegacyGameState state = hit();
                        if (isGameOver(state)) {
                            printFinalState(state);
                            running = false;
                        }
                    }

                    case "2" -> {
                        LegacyGameState state = stand();
                        printFinalState(state);
                        running = false;
                    }

                    default -> System.out.println("Invalid choice. Please select 1 or 2.");
                }
            }

            System.out.println("Game over.");

            boolean answered = false;

            do {

                System.out.println("\nKeep playing?");
                System.out.println("1 - Yes");
                System.out.println("2 - No");
                System.out.print("> ");

                String input = scanner.nextLine();

                switch (input) {

                    case "1" -> {
                        answered = true;
                    }

                    case "2" -> {
                        stillPlay = false;
                        answered = true;
                    }

                    default -> System.out.println("Invalid choice. Please select 1 or 2.");
                }
            } while (!answered);
        } while (stillPlay);
    }

    private void createGameInDatabase() {
        String sql = "INSERT INTO GAME (GAME_CREATION_DATE, GAME_STATE) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ps.setString(2, LegacyGameState.ONGOING.name());
            return ps;
        }, keyHolder);

        gameId = keyHolder.getKey().longValue();
    }

    public static int calculateScore(List<Card> cards) {
        int sum = 0;

        for (var card : cards) {
            if (!isAce(card)) {
                sum += getCardValue(card);
            }
        }

        for (var card : cards) {
            if (isAce(card)) {
                if (sum + 11 > 21) {
                    sum += 1;
                }
                else {
                    sum += 11;
                }
            }
        }
        return sum;
    }

    private void printStatistics() {
        System.out.println(" Win percentage : " + showPercentage(statistics.getWinPercentage()));
    }

    public static int calculateScore(Long gameId) {

        List<LegacyCardEntity> cardsEntity = getCardsFromDatabase(gameId);

        List<Card> cards = cardsEntity.stream()
                .filter(cardEntity -> cardEntity.playerId() != 0L)
                .map(cardEntity -> new Card(
                        FrenchRank.fromStrength(cardEntity.cardRank()),
                        FrenchSuit.from(cardEntity.cardColor())
                )).toList();

        return calculateScore(cards);
    }

    public static int calculateNbCards(Long gameId) {

        List<LegacyCardEntity> cardsEntity = getCardsFromDatabase(gameId);

        return Math.toIntExact(cardsEntity.stream()
                .filter(cardEntity -> cardEntity.playerId() != 0L)
                .count());
    }












    /*
     * ===========================================================================
     * =                                                                         =
     * ===========================================================================
     */

    public LegacyGameState createGame() {
        deck = deckFactory.generate(DeckType.FRENCH);
        logger.write("New game");
        createGameInDatabase();

        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();

        playerDraw();
        dealerDraw();
        playerDraw();
        dealerDraw();

        if (isBlackjack(playerCards) && isBlackjack(dealerCards)) {
            gameState = LegacyGameState.TIE;
        } else if (isBlackjack(playerCards)) {
            gameState = LegacyGameState.WIN;
        } else if (isBlackjack(dealerCards)) {
            gameState = LegacyGameState.LOSE;
        } else {
            gameState = LegacyGameState.ONGOING;
        }

        updateGameStateInDatabase();
        return gameState;
    }

    public LegacyGameState hit() {
        playerDraw();

        if (calculateScore(playerCards) > 21) {
            logger.write("Player exceeded 21 and lost");
            gameState = LegacyGameState.LOSE;
            updateGameStateInDatabase();
            return gameState;
        }

        return gameState;
    }

    public LegacyGameState stand() {
        logger.write("Player stands.");

        dealerDrawUntil17();

        if (calculateScore(dealerCards) > 21) {
            logger.write("Dealer exceeded 21. Player wins");
            gameState = LegacyGameState.WIN;
        }
        else if (calculateScore(dealerCards) > calculateScore(playerCards)) {
            logger.write("Dealer is the closest to 21. Player lost");
            gameState = LegacyGameState.LOSE;
        }
        else if (calculateScore(dealerCards) == calculateScore(playerCards)) {
            logger.write("Player and dealer have the same score. Tie");
            gameState = LegacyGameState.TIE;
        }
        else if (calculateScore(dealerCards) < calculateScore(playerCards)) {
            logger.write("Player is the closest to 21 and wins");
            gameState = LegacyGameState.WIN;
        }
        else {
            throw new IllegalStateException("Game should be ended if player stands");
        }

        updateGameStateInDatabase();

        return gameState;
    }

    private void updateGameStateInDatabase() {
        jdbcTemplate.update(
                "UPDATE GAME SET GAME_STATE = ? WHERE GAME_ID = ?",
                gameState.name(),
                gameId
        );
    }

    private List<LegacyGameEntity> getGameList() {
        List<LegacyGameEntity> gamesDB = jdbcTemplate.query("SELECT * FROM GAME",
                (rs, rowNum) -> new LegacyGameEntity(
                        rs.getLong("GAME_ID"),
                        rs.getDate("GAME_CREATION_DATE"),
                        rs.getDate("GAME_CREATION_DATE"),
                        rs.getLong("GAME_ID"),
                        "Name",
                        rs.getString("GAME_STATE")
                ));

        return gamesDB;
    }

    public Long getGameId() {
        return gameId;
    }

    public int getPlayerScore() {
        return calculateScore(playerCards);
    }

    public int getDealerScore() {
        return calculateScore(dealerCards);
    }

    private void dealerDrawUntil17() {
        while(calculateScore(dealerCards) < 17) {
            draw(dealerCards);
        }
    }

    public boolean playerDraw() {
        boolean drawed = draw(playerCards);
        Card cardDrawn = playerCards.get(playerCards.size()-1);
        logger.forceWrite("Player drew "+cardDrawn.getRank()+" of "+cardDrawn.getSuit());
        saveMoveInDatabase(false, cardDrawn);

        return drawed;
    }

    public boolean dealerDraw() {
        boolean drawed = draw(dealerCards);
        Card cardDrawn = dealerCards.get(dealerCards.size()-1);
        logger.forceWrite("Dealer drew "+cardDrawn.getRank()+" of "+cardDrawn.getSuit());
        saveMoveInDatabase(true, cardDrawn);

        return drawed;
    }

    private void saveMoveInDatabase(boolean isDealer, Card cardDrawn) {
        jdbcTemplate.update(
                "INSERT INTO PILE(PILE_FK_GAME_ID, PILE_PLAYER_ID, PILE_CARD_RANK, PILE_CARD_COLOR) " +
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
        if (calculateScore(cards) > 21) {
            return false;
        }

        return true;
    }

    public void loadGame(Long gameId) {
        LegacyGameEntity gameDB = jdbcTemplate.queryForObject("SELECT * FROM GAME WHERE GAME_ID = ?", new Object[]{gameId},
                (rs, rowNum) -> new LegacyGameEntity(
                        rs.getLong("GAME_ID"),
                        rs.getDate("GAME_CREATION_DATE"),
                        rs.getDate("GAME_CREATION_DATE"),
                        rs.getLong("GAME_ID"),
                        "Name",
                        rs.getString("GAME_STATE")
                ));

        List<LegacyCardEntity> pilesDB = getCardsFromDatabase(gameId);

        if (gameDB == null) {
            return;
        }

        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();

        for (LegacyCardEntity cardEntity : pilesDB) {
            if (cardEntity.playerId() == 0L) {
                dealerCards.add(new Card(
                        FrenchRank.fromStrength(cardEntity.cardRank()),
                        FrenchSuit.from(cardEntity.cardColor())
                ));
            }
            else {
                playerCards.add(new Card(
                        FrenchRank.fromStrength(cardEntity.cardRank()),
                        FrenchSuit.from(cardEntity.cardColor())
                ));
            }
        }

        deck = deckFactory.generate(DeckType.FRENCH);

        for (Card card : playerCards) {
            deck.remove(card);
        }
        for (Card card : dealerCards) {
            deck.remove(card);
        }
        logger.write("Loaded game "+gameId);

        this.gameId = gameId;
        gameState = LegacyGameState.from(gameDB.state());
    }

    private static List<LegacyCardEntity> getCardsFromDatabase(Long gameId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        List<LegacyCardEntity> pilesDB = jdbcTemplate.query("SELECT * FROM PILE WHERE PILE_FK_GAME_ID = ?", new Object[]{gameId},
                (rs, rowNum) -> new LegacyCardEntity(
                        rs.getLong("PILE_ID"),
                        rs.getInt("PILE_PLAYER_ID"),
                        rs.getInt("PILE_CARD_RANK"),
                        rs.getString("PILE_CARD_COLOR")
                ));
        return pilesDB;
    }

    private void printScores() {
        System.out.println("\n--- Current Scores ---");
        System.out.println("Player: " + getPlayerScore());
        System.out.println("Dealer: " + getDealerScore());
    }

    private boolean isGameOver(LegacyGameState state) {
        return state == LegacyGameState.WIN
                || state == LegacyGameState.LOSE
                || state == LegacyGameState.TIE;
    }

    private boolean isBlackjack(List<Card> cards) {
        return calculateScore(cards) == 21 && cards.size() == 2;
    }

    private static boolean isAce(Card card) {
        return ACE.equals(card.getRank());
    }

    private static Integer getCardValue(Card card) {
        return ((FrenchRank) card.getRank()).getValue();
    }

    private void printFinalState(LegacyGameState state) {

        printScores();

        System.out.println("\n===== RESULT =====");

        if (isBlackjack(playerCards)) {
            System.out.println("BLACKJACK!!! 🂡🂱 You hit 21 with two cards!");
        }

        switch (state) {
            case WIN -> System.out.println("You win! 🎉");
            case LOSE -> System.out.println("You lose.");
            case TIE -> System.out.println("It's a tie.");
            default -> System.out.println("Game ended.");
        }
    }

    private String showPercentage(double percentage) {
        DecimalFormat df = new DecimalFormat("#.##");

        return df.format(percentage) + " %";
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

    public static HikariDataSource getTestDataSource() {
        if (dataSource != null) {
            return dataSource;
        }

        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(JDBC_TEST_URL);
        dataSource.setUsername(JDBC_USERNAME);
        dataSource.setPassword(JDBC_PASSWORD);

        return dataSource;
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
