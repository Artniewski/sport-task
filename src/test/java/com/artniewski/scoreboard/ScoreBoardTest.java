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
            Match match = scoreBoard.startGame("Mexico", "Canada");
            // Then
            assertNotNull(match);
            assertEquals("Mexico", match.getHomeTeam());
            assertEquals(0, match.getHomeScore());
            assertEquals("Canada", match.getAwayTeam());
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
            assertThrows(GameException.class, () -> scoreBoard.startGame("Brazil", "Brazil"));
        }

        @Test
        void shouldThrowExceptionIfAnyTeamAlreadyInGame() {
            // Given
            scoreBoard.startGame("Spain", "Italy");
            // When & Then
            assertThrows(GameException.class, () -> scoreBoard.startGame("Spain", "France"));
            assertThrows(GameException.class, () -> scoreBoard.startGame("Germany", "Italy"));
        }

        @Test
        void shouldIncludeStartedGameInSummary() {
            // Given
            Match match = scoreBoard.startGame("Brazil", "Argentina");
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
            assertThrows(GameNotFoundException.class, () -> scoreBoard.finishGame("Brazil", "Argentina"));
        }

        @Test
        void shouldSuccessfullyFinishGame() {
            // Given
            scoreBoard.startGame("Brazil", "Argentina");
            // When & Then
            assertDoesNotThrow(() -> scoreBoard.finishGame("Brazil", "Argentina"));
        }

        @Test
        void shouldRemoveGameFromSummaryAfterFinish() {
            // Given
            scoreBoard.startGame("Brazil", "Argentina");
            scoreBoard.finishGame("Brazil", "Argentina");
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
            assertThrows(GameNotFoundException.class, () -> scoreBoard.updateScore("Brazil", "Argentina", 2, 3));
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
            scoreBoard.startGame("Brazil", "Argentina");
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> scoreBoard.updateScore("Brazil", "Argentina", homeScore, awayScore));
        }

        @Test
        void shouldUpdateGameScore() {
            // Given
            Match match = scoreBoard.startGame("Brazil", "Argentina");
            // When
            Match result = scoreBoard.updateScore("Brazil", "Argentina", 2, 3);
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
                Arguments.of("", "Argentina"),
                Arguments.of(null, "Argentina"),
                Arguments.of("Brazil", ""),
                Arguments.of("Brazil", null)
        );
    }

}