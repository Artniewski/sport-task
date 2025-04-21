package com.artniewski.scoreboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

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

    @Test
    void shouldThrowExceptionForInvalidTeamName() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.startGame("", "Poland"));
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.startGame(null, "Germany"));
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
}