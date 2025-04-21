package com.artniewski.scoreboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}