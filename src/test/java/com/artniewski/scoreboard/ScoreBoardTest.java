package com.artniewski.scoreboard;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.artniewski.scoreboard.exception.GameException;
import com.artniewski.scoreboard.exception.GameNotFoundException;

class ScoreBoardTest {

    @Test
    void shouldStartGameWithInitialScore() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
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
    @MethodSource("invalidTeamNames")
    void shouldThrowExceptionForInvalidTeamNameOnStart(String homeTeamName, String awayTeamName) {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.startGame(homeTeamName, awayTeamName));
    }

    @Test
    void shouldThrowGameExceptionForSameTeamNames() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        // When & Then
        assertThrows(GameException.class, () -> scoreBoard.startGame("Brazil", "Brazil"));
    }

    @Test
    void shouldThrowExceptionIfAnyTeamAlreadyInGame() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame("Spain", "Italy");
        // When & Then
        assertThrows(GameException.class, () -> scoreBoard.startGame("Spain", "France"));
        assertThrows(GameException.class, () -> scoreBoard.startGame("Germany", "Italy"));
    }

    @Test
    void shouldThrowExceptionWhenFinishingNonExistentGame() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        // When & Then
        assertThrows(GameNotFoundException.class, () -> scoreBoard.finishGame("Brazil", "Argentina"));
    }

    @Test
    void shouldSuccessfullyFinishGame() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame("Brazil", "Argentina");
        // When & Then
        assertDoesNotThrow(() -> scoreBoard.finishGame("Brazil", "Argentina"));
    }

    @Test
    void shouldReturnSummaryWithEmptyList() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        // When
        List<Match> summary = scoreBoard.getSummary();
        // Then
        assertTrue(summary.isEmpty());
    }

    @Test
    void shouldReturnSummaryWithStartedGame() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        Match match = scoreBoard.startGame("Brazil", "Argentina");
        // When
        List<Match> summary = scoreBoard.getSummary();
        // Then
        assertEquals(1, summary.size());
        assertIterableEquals(List.of(match), summary);
    }

    @Test
    void shouldReturnEmptySummaryAfterFinishingGame() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame("Brazil", "Argentina");
        scoreBoard.finishGame("Brazil", "Argentina");
        // When
        List<Match> summary = scoreBoard.getSummary();
        // Then
        assertTrue(summary.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidTeamNames")
    void shouldThrowExceptionForInvalidTeamNameOnFinish(String homeTeamName, String awayTeamName) {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.finishGame(homeTeamName, awayTeamName));
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