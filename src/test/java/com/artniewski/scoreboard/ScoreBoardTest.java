package com.artniewski.scoreboard;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.artniewski.scoreboard.exception.GameException;
import com.artniewski.scoreboard.exception.GameNotFoundException;

class ScoreBoardTest {

    static class Teams {
        public static final String MEXICO = "Mexico";
        public static final String CANADA = "Canada";
        public static final String SPAIN = "Spain";
        public static final String BRAZIL = "Brazil";
        public static final String GERMANY = "Germany";
        public static final String FRANCE = "France";
        public static final String ITALY = "Italy";
        public static final String ARGENTINA = "Argentina";
    }

    ScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoard();
    }

    @Nested
    class StartGameTests {
        @Test
        void shouldStartGameWithInitialScore() {
            // When
            Match match = scoreBoard.startGame(Teams.MEXICO, Teams.CANADA);
            // Then
            assertNotNull(match);
            assertEquals(Teams.MEXICO, match.getHomeTeam());
            assertEquals(0, match.getHomeScore());
            assertEquals(Teams.CANADA, match.getAwayTeam());
            assertEquals(0, match.getAwayScore());
        }

        @ParameterizedTest
        @MethodSource("com.artniewski.scoreboard.ScoreBoardTest#invalidTeamNames")
        void shouldThrowExceptionForInvalidTeamNameOnStart(String homeTeamName, String awayTeamName) {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> scoreBoard.startGame(homeTeamName, awayTeamName));
        }

        @Test
        void shouldThrowGameExceptionForSameTeamNames() {
            // When & Then
            assertThrows(GameException.class, () -> scoreBoard.startGame(Teams.BRAZIL, Teams.BRAZIL));
        }

        @Test
        void shouldThrowExceptionIfAnyTeamAlreadyInGame() {
            // Given
            scoreBoard.startGame(Teams.SPAIN, Teams.ITALY);
            // When & Then
            assertThrows(GameException.class, () -> scoreBoard.startGame(Teams.SPAIN, Teams.FRANCE));
            assertThrows(GameException.class, () -> scoreBoard.startGame(Teams.GERMANY, Teams.ITALY));
        }

        @Test
        void shouldIncludeStartedGameInSummary() {
            // Given
            Match match = scoreBoard.startGame(Teams.BRAZIL, Teams.ARGENTINA);
            // When
            List<Match> summary = scoreBoard.getSummary();
            // Then
            assertEquals(1, summary.size());
            assertIterableEquals(List.of(match), summary);
        }
    }

    @Nested
    class FinishGameTests {
        @Test
        void shouldThrowExceptionWhenFinishingNonExistentGame() {
            // When & Then
            assertThrows(GameNotFoundException.class, () -> scoreBoard.finishGame(Teams.BRAZIL, Teams.ARGENTINA));
        }

        @Test
        void shouldSuccessfullyFinishGame() {
            // Given
            scoreBoard.startGame(Teams.BRAZIL, Teams.ARGENTINA);
            // When & Then
            assertDoesNotThrow(() -> scoreBoard.finishGame(Teams.BRAZIL, Teams.ARGENTINA));
        }

        @Test
        void shouldRemoveGameFromSummaryAfterFinish() {
            // Given
            scoreBoard.startGame(Teams.BRAZIL, Teams.ARGENTINA);
            scoreBoard.finishGame(Teams.BRAZIL, Teams.ARGENTINA);
            // When
            List<Match> summary = scoreBoard.getSummary();
            // Then
            assertTrue(summary.isEmpty());
        }

        @ParameterizedTest
        @MethodSource("com.artniewski.scoreboard.ScoreBoardTest#invalidTeamNames")
        void shouldThrowExceptionForInvalidTeamNameOnFinish(String homeTeamName, String awayTeamName) {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> scoreBoard.finishGame(homeTeamName, awayTeamName));
        }
    }

    @Nested
    class UpdateGameTests {
        @Test
        void shouldThrowExceptionUpdatingScoreForNonExistentGame() {
            // When & Then
            assertThrows(GameNotFoundException.class, () -> scoreBoard.updateScore(Teams.BRAZIL, Teams.ARGENTINA, 2, 3));
        }

        @ParameterizedTest
        @MethodSource("com.artniewski.scoreboard.ScoreBoardTest#invalidTeamNames")
        void shouldThrowExceptionForInvalidTeamNameOnUpdate(String homeTeamName, String awayTeamName) {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> scoreBoard.updateScore(homeTeamName, awayTeamName, 2, 3));
        }

        @ParameterizedTest
        @MethodSource("invalidGameScores")
        void shouldThrowExceptionUpdatingWithInvalidScore(Integer homeScore, Integer awayScore) {
            scoreBoard.startGame(Teams.BRAZIL, Teams.ARGENTINA);
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> scoreBoard.updateScore(Teams.BRAZIL, Teams.ARGENTINA, homeScore, awayScore));
        }

        @Test
        void shouldUpdateGameScore() {
            // Given
            Match match = scoreBoard.startGame(Teams.BRAZIL, Teams.ARGENTINA);
            // When
            Match result = scoreBoard.updateScore(Teams.BRAZIL, Teams.ARGENTINA, 2, 3);
            // Then
            assertEquals(2, result.getHomeScore());
            assertEquals(3, result.getAwayScore());
        }

        private static Stream<Arguments> invalidGameScores() {
            return Stream.of(
                    Arguments.of(-3, 0),
                    Arguments.of(0, -10),
                    Arguments.of(-1, -1)
            );
        }
    }

    @Nested
    class SummaryTests {

        @Test
        void shouldReturnSummaryWithEmptyList() {
            // When
            List<Match> summary = scoreBoard.getSummary();
            // Then
            assertTrue(summary.isEmpty());
        }
    }

    private static Stream<Arguments> invalidTeamNames() {
        return Stream.of(
                Arguments.of("", Teams.ARGENTINA),
                Arguments.of(null, Teams.ARGENTINA),
                Arguments.of(Teams.BRAZIL, ""),
                Arguments.of(Teams.BRAZIL, null)
        );
    }

}