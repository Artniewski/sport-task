package com.artniewski.scoreboard;

public class ScoreBoard {
    public Match startGame(String homeTeam, String awayTeam) {
        if (homeTeam == null || homeTeam.trim().isEmpty()) {
            throw new IllegalArgumentException("Home team name cannot be null or empty");
        }
        if (awayTeam == null || awayTeam.trim().isEmpty()) {
            throw new IllegalArgumentException("Away team name cannot be null or empty");
        }
        return new Match(homeTeam, awayTeam);
    }
}
