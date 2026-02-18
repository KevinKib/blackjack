package org.kevinkib;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.Deck;
import org.kevinkib.cards.domain.DeckType;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;
import org.kevinkib.cards.domain.french.FrenchRank;
import org.kevinkib.cards.domain.french.FrenchSuit;
import org.springframework.jdbc.core.JdbcTemplate;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackJackService {

    private Deck deck;
    private FrenchDeckFactory deckFactory;
    private final Logger logger = new Logger(false);
    private final JdbcTemplate jdbcTemplate;
    private Long gameId = 0L;
    private GameState gameState;

    public static final String JDBC_URL = "jdbc:h2:file:./src/main/data/resources";
    public static final String JDBC_USERNAME = "sa";
    public static final String JDBC_PASSWORD = "";
    public static HikariDataSource dataSource;

    private final Scanner scanner;

    private List<Card> playerCards;
    private List<Card> dealerCards;

    public BlackJackService(FrenchDeckFactory deckFactory) {
        this.deckFactory = deckFactory;
        this.jdbcTemplate = new JdbcTemplate(getDataSource());
        this.scanner = new Scanner(System.in);
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

        updateGameStateInDatabase();
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
            updateGameStateInDatabase();
            return gameState;
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

        updateGameStateInDatabase();

        return gameState;
    }

    public GameState stand() {
        logger.write("Player stands.");

        dealerDrawUntil17();

        if (sumCards(dealerCards) > 21) {
            logger.write("Dealer exceeded 21. Player wins");
            gameState = GameState.WIN;
        }
        else if (sumCards(dealerCards) > sumCards(playerCards)) {
            logger.write("Dealer is the closest to 21. Player lost");
            gameState = GameState.LOSE;
        }
        else if (sumCards(dealerCards) == sumCards(playerCards)) {
            logger.write("Player and dealer have the same score. Tie");
            gameState = GameState.TIE;
        }
        else if (sumCards(dealerCards) < sumCards(playerCards)) {
            logger.write("Player is the closest to 21 and wins");
            gameState = GameState.WIN;
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

    private List<GameEntity> getGameList() {
        List<GameEntity> gamesDB = jdbcTemplate.query("SELECT * FROM GAME",
                (rs, rowNum) -> new GameEntity(
                        rs.getLong("GAME_ID"),
                        rs.getDate("GAME_CREATION_DATE"),
                        rs.getString("GAME_STATE")
                ));

        return gamesDB;
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
        logger.forceWrite("Player drew "+cardDrawn.getRank());
        saveMoveInDatabase(false, cardDrawn);

        return drawed;
    }

    public boolean dealerDraw() {
        boolean drawed = draw(dealerCards);
        Card cardDrawn = dealerCards.get(dealerCards.size()-1);
        logger.forceWrite("Dealer drew "+cardDrawn.getRank());
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
        if (sumCards(cards) > 21) {
            return false;
        }

        return true;
    }

    public int sumCards(List<Card> cards) {
        int sum = 0;
        for (var card : cards) {
            if (!FrenchRank.ACE.equals(card.getRank())) {
                sum += ((FrenchRank)card.getRank()).getValue();
            }
        }
        for (var card : cards) {
            if (FrenchRank.ACE.equals(card.getRank())) {
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

    public void loadGame(Long gameId) {
        GameEntity gameDB = jdbcTemplate.queryForObject("SELECT * FROM GAME WHERE GAME_ID = ?", new Object[]{gameId},
                (rs, rowNum) -> new GameEntity(
                        rs.getLong("GAME_ID"),
                        rs.getDate("GAME_CREATION_DATE"),
                        rs.getString("GAME_STATE")
                ));

        List<PileEntity> pilesDB = jdbcTemplate.query("SELECT * FROM PILE WHERE PILE_FK_GAME_ID = ?", new Object[]{gameId},
                (rs, rowNum) -> new PileEntity(
                        rs.getLong("PILE_ID"),
                        rs.getInt("PILE_PLAYER_ID"),
                        rs.getInt("PILE_CARD_RANK"),
                        rs.getString("PILE_CARD_COLOR")
                ));

        if (gameDB == null) {
            return;
        }

        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();

        for (PileEntity pileEntity : pilesDB) {
            if (pileEntity.playerId() == 0L) {
                dealerCards.add(new Card(
                        FrenchRank.fromStrength(pileEntity.cardRank()),
                        FrenchSuit.from(pileEntity.cardColor())
                ));
            }
            else {
                playerCards.add(new Card(
                        FrenchRank.fromStrength(pileEntity.cardRank()),
                        FrenchSuit.from(pileEntity.cardColor())
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
        gameState = GameState.from(gameDB.state());
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

            List<GameEntity> gameDBs = getGameList();
            System.out.println(" Number of games played : "+gameDBs.size());

            if (!gameDBs.isEmpty()) {
                Long wonGames = gameDBs.stream().filter(gameDB -> gameDB.state().equals(GameState.WIN.name())).count();
                double winRate = (double) wonGames / gameDBs.size() * 100;
                System.out.println(" Win percentage : "+ winRate + "%");
            }

            createGame();

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
                        GameState state = hit();
                        if (isGameOver(state)) {
                            printFinalState(state);
                            running = false;
                        }
                    }

                    case "2" -> {
                        GameState state = stand();
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

    private void printScores() {
        System.out.println("\n--- Current Scores ---");
        System.out.println("Player: " + getPlayerScore());
        System.out.println("Dealer: " + getDealerScore());
    }

    private boolean isGameOver(GameState state) {
        return state == GameState.WIN
                || state == GameState.LOSE
                || state == GameState.TIE;
    }

    private boolean isBlackjack(List<Card> cards) {
        return sumCards(cards) == 21 && cards.size() == 2;
    }

    private void printFinalState(GameState state) {

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

    /*

    règles:
    - un joueur, un croupier; le joueur commence
    - le joueur pioche directement une carte
    - un joueur a pour somme la somme des cartes (têtes à 10, as à 11)
    - si un joueur dépasse 21, il perd
    - sinon, le joueur le plus proche de 21 gagne

     */


}
